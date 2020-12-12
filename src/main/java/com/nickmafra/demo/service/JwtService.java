package com.nickmafra.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Clock;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nickmafra.demo.infra.exception.AppAuthenticationException;
import com.nickmafra.demo.infra.exception.TokenExpiradoException;
import com.nickmafra.demo.infra.exception.TokenInvalidoException;
import com.nickmafra.demo.infra.properties.JwtProperties;
import com.nickmafra.demo.infra.security.JwtAuthentication;
import com.nickmafra.demo.infra.security.JwtUserDetails;
import com.nickmafra.demo.infra.security.Papel;
import com.nickmafra.demo.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtService {

    private static final String ROLES_CLAIM_NAME = "roles";

    @Autowired
    private JwtProperties properties;
    @Autowired
    private X509Service x509Service;
    @Autowired
    private DateService dateService;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    private void init() {
        algorithm = createAlgorithm(x509Service.getPublicKey(), x509Service.getPrivateKey());
        verifier = createVerifier(algorithm, () -> toDate(dateService.agora()), properties.getIssuer());
    }

    private static Algorithm createAlgorithm(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        return Algorithm.RSA256(publicKey, privateKey);
    }

    private static JWTVerifier createVerifier(Algorithm algorithm, Clock clock, String issuer) {
        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(algorithm)
                .withIssuer(issuer);
        return verification
                .build(clock);
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(dateService.zone()).toInstant());
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.from(date.toInstant().atZone(dateService.zone()));
    }

    public String gerarToken(JwtUserDetails jwtUserDetails) {
        LocalDateTime agora = dateService.agora();
        LocalDateTime dataExp = agora.plus(properties.getDuration());

        return JWT.create()
                .withIssuer(properties.getIssuer())
                .withSubject(String.valueOf(jwtUserDetails.getIdUsuario()))
                .withIssuedAt(toDate(agora))
                .withExpiresAt(toDate(dataExp))
                .withArrayClaim(ROLES_CLAIM_NAME, Papel.toStringArray(jwtUserDetails.getPapeis()))
                .sign(algorithm);
    }

    public String gerarToken(Usuario usuario) {
        return gerarToken(new JwtUserDetails(usuario));
    }

    private DecodedJWT decode(String strToken) throws TokenInvalidoException {
        try {
            return JWT.decode(strToken);
        } catch (JWTDecodeException e) {
            throw new TokenInvalidoException(e);
        }
    }

    public JwtUserDetails lerToken(String strToken) throws TokenInvalidoException, TokenExpiradoException {
        return parse(decode(strToken));
    }

    public JwtAuthentication parse(String strToken) throws TokenInvalidoException {
        return new JwtAuthentication(decode(strToken));
    }

    private JwtUserDetails parse(DecodedJWT decodedJWT) throws TokenInvalidoException, TokenExpiradoException {
        try {
            // verifica a assinatura e se a data de expiração é anterior a hoje
            verifier.verify(decodedJWT);

            // necessário caso a duration tenha sido diminuída e o token seja antigo
            validarDataEmissao(toLocalDateTime(decodedJWT.getIssuedAt()));

            long idUsuario = Long.parseLong(decodedJWT.getSubject());

            String[] roles = decodedJWT.getClaim(ROLES_CLAIM_NAME).asArray(String.class);
            Collection<Papel> papeis = Papel.fromStringArray(roles);

            return new JwtUserDetails(idUsuario, papeis);

        } catch (TokenExpiredException e) {
            throw new TokenExpiradoException(e);
        } catch (JWTVerificationException e) {
            throw new TokenInvalidoException("Erro ao verificar token JWT.", e);
        }
    }

    private void validarDataEmissao(LocalDateTime dataEmissao) throws TokenExpiradoException {
        LocalDateTime dataExpiracaoCalculada = dataEmissao.plus(properties.getDuration());
        if (dataExpiracaoCalculada.isBefore(dateService.agora()))
            throw new TokenExpiradoException();
    }

    public JwtAuthentication autenticar(JwtAuthentication jwtAuthentication) {
        try {
            DecodedJWT decodedJWT = jwtAuthentication.getCredentials();
            JwtUserDetails jwtUserDetails = parse(decodedJWT);
            return new JwtAuthentication(decodedJWT, jwtUserDetails);
        } catch (Exception e) {
            throw new AppAuthenticationException("Autenticação por JWT falhou.", e);
        }
    }

}
