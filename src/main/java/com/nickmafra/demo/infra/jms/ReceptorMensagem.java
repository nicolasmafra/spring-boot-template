package com.nickmafra.demo.infra.jms;

import com.nickmafra.demo.infra.jms.dto.MensagemJmsDto;
import com.nickmafra.demo.service.MensagemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReceptorMensagem {

    @Autowired
    private MensagemService mensagemService;

    @JmsListener(destination = JmsConstants.DESTINATION)
    public void receberMensagem(MensagemJmsDto dto) throws InterruptedException {
        log.info("Recebendo mensagem JMS com título: {}", dto.getTitulo());
        log.debug("Simulando processamento demorado");
        Thread.sleep(10000);
        mensagemService.salvar(dto.toMensagem());
    }
}
