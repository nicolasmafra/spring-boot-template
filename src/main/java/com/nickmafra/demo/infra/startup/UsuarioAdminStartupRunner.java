package com.nickmafra.demo.infra.startup;

import com.nickmafra.demo.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsuarioAdminStartupRunner implements ApplicationRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(ApplicationArguments args) {
        log.debug("Criando usuário admin...");
        usuarioService.criarAdmin();
        log.info("Usuário admin criado/atualizado.");
    }
}
