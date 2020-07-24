package com.nickmafra.demo.service;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

class CriptoServiceTest {

    @Test
    void conferirSenhaOfuscada() throws NoSuchAlgorithmException {
        CriptoService criptoService = new CriptoService();
        String senha = "Teste1!";

        String hash = criptoService.ofuscarSenha(senha);
        boolean confere = criptoService.conferirSenhaOfuscada(senha, hash);
        assertThat(confere).isTrue();
    }
}