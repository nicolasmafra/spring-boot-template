package com.nickmafra.demo.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles({ "test", "mockDate" })
class JwtServiceTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    DateServiceImplMock dateServiceImplMock;

    @Test
    void gerarLerToken() {
        long id = 31;

        String token = jwtService.gerarToken(id);
        log.debug("Token JWT: {}", token);
        long idLido = jwtService.lerToken(token);

        assertThat(idLido).isEqualTo(id);
    }

    @Test
    void naoExpirado() {
        long id = 37;
        // expira em 15 minutos
        ZonedDateTime data1 = ZonedDateTime.of(2020, 8, 21, 14, 45, 0, 0, ZoneOffset.UTC);
        ZonedDateTime data2 = ZonedDateTime.of(2020, 8, 21, 14, 59, 59, 0, ZoneOffset.UTC);

        dateServiceImplMock.zonedDateTime = data1;
        String token = jwtService.gerarToken(id);

        dateServiceImplMock.zonedDateTime = data2;
        long idLido = jwtService.lerToken(token);

        assertThat(idLido).isEqualTo(id);
    }

    @Test
    void expirado() {
        long id = 37;
        // expira em 15 minutos
        ZonedDateTime data1 = ZonedDateTime.of(2020, 8, 21, 14, 45, 0, 0, ZoneOffset.UTC);
        ZonedDateTime data2 = ZonedDateTime.of(2020, 8, 21, 15, 0, 1, 0, ZoneOffset.UTC);

        dateServiceImplMock.zonedDateTime = data1;
        String token = jwtService.gerarToken(id);

        dateServiceImplMock.zonedDateTime = data2;
        Assertions.assertThrows(TokenExpiredException.class, () -> jwtService.lerToken(token));
    }
}