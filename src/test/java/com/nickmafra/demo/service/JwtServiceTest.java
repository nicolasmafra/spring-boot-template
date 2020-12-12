package com.nickmafra.demo.service;

import com.nickmafra.demo.infra.exception.TokenExpiradoException;
import com.nickmafra.demo.infra.exception.TokenInvalidoException;
import com.nickmafra.demo.infra.security.JwtUserDetails;
import com.nickmafra.demo.infra.security.Papel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
@SpringBootTest
@ActiveProfiles({ "test", "mockDate" })
class JwtServiceTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    DateServiceImplMock dateServiceImplMock;

    @Test
    void gerarLerToken() throws TokenInvalidoException, TokenExpiradoException {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(31, Papel.ADMIN);
        dateServiceImplMock.setAgora(null);

        String token = jwtService.gerarToken(jwtUserDetails);
        log.debug("Token JWT: {}", token);
        JwtUserDetails jwtUserDetailsLido = jwtService.lerToken(token);

        assertThat(jwtUserDetails).isEqualTo(jwtUserDetailsLido);
    }

    @Test
    void naoExpirado() throws TokenInvalidoException, TokenExpiradoException {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(37, Papel.ADMIN);

        // expira em 15 minutos
        LocalDateTime data1 = LocalDateTime.of(2020, 8, 21, 14, 45, 0, 0);
        LocalDateTime data2 = LocalDateTime.of(2020, 8, 21, 14, 59, 59, 0);

        dateServiceImplMock.setAgora(data1);
        String token = jwtService.gerarToken(jwtUserDetails);

        dateServiceImplMock.setAgora(data2);
        JwtUserDetails jwtUserDetailsLido = jwtService.lerToken(token);

        assertThat(jwtUserDetails).isEqualTo(jwtUserDetailsLido);
    }

    @Test
    void expirado() {
        JwtUserDetails jwtUserDetails = new JwtUserDetails(37, Papel.ADMIN);
        dateServiceImplMock.setAgora(null);

        // expira em 15 minutos
        LocalDateTime data1 = LocalDateTime.of(2020, 8, 21, 14, 45, 0, 0);
        LocalDateTime data2 = LocalDateTime.of(2020, 8, 21, 15, 0, 1, 0);

        dateServiceImplMock.setAgora(data1);
        String token = jwtService.gerarToken(jwtUserDetails);

        dateServiceImplMock.setAgora(data2);
        assertThatExceptionOfType(TokenExpiradoException.class).isThrownBy(() -> jwtService.lerToken(token));
    }
}