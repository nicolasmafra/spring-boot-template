package com.nickmafra.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles({ "test" })
class X509ServiceTest {

    @Autowired
    X509Service x509Service;

    @Test
    void encrypt_decrypt() {
        String textoOriginal = " 297SD-_/?@cnik ";

        String textoCodificado = x509Service.encrypt(textoOriginal);
        assertThat(textoCodificado).isNotEqualTo(textoOriginal);

        log.debug(textoCodificado);

        String textoDecodificado = x509Service.decrypt(textoCodificado);
        assertThat(textoDecodificado).isEqualTo(textoOriginal);
    }

}