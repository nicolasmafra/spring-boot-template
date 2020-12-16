package com.nickmafra.demo.infra.startup;

import com.nickmafra.demo.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsuarioAdminStartupRunner {

    @Autowired
    private UsuarioService usuarioService;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        log.debug("Criando usuário admin...");
        usuarioService.criarAdmin();
        log.info("Usuário admin criado/atualizado.");
    }
}
