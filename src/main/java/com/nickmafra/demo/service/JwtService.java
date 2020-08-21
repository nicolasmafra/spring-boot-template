package com.nickmafra.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Clock;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nickmafra.demo.infra.exception.AppRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private static String issuer = "spring-boot-template";

    @Autowired
    private X509Service x509Service;
    @Autowired
    private DateService dateService;

    private Algorithm algorithm;
    private JWTVerifier verifier;
    private Duration duration = Duration.of(15, ChronoUnit.MINUTES);

    @PostConstruct
    private void init() {
        algorithm = createAlgorithm(x509Service.getPublicKey(), x509Service.getPrivateKey());
        verifier = createVerifier(algorithm, () -> toDate(dateService.zonedAgora()));
    }

    private static Algorithm createAlgorithm(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        return Algorithm.RSA256(publicKey, privateKey);
    }

    private static JWTVerifier createVerifier(Algorithm algorithm, Clock clock) {
        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(algorithm)
                .withIssuer(issuer);
        return verification
                .build(clock);
    }

    private static Date toDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public String gerarToken(long idUsuario) {
        ZonedDateTime zonedAgora = dateService.zonedAgora();
        Date dateAgora = toDate(zonedAgora);
        Date dateExp = toDate(zonedAgora.plus(duration));

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(String.valueOf(idUsuario))
                .withIssuedAt(dateAgora)
                .withExpiresAt(dateExp)
                .sign(algorithm);
    }

    public long lerToken(String strToken) {
        DecodedJWT decodedJWT = verifier.verify(strToken);
        try {
            return Long.parseLong(decodedJWT.getSubject());
        } catch (NumberFormatException e) {
            throw new AppRuntimeException("O id de usuário dentro do token é inválido.", e);
        }
    }

}
