package com.nickmafra.demo.infra.jms;

import com.nickmafra.demo.dto.MensagemDto;
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
    public void receberMensagem(String stringMensagem) throws InterruptedException {
        MensagemDto mensagem = MensagemDto.deserializar(stringMensagem);
        log.info("Recebendo mensagem com título: {}", mensagem.getTitulo());
        log.debug("Simulando processamento demorado");
        Thread.sleep(10000);
        log.debug("Salvando mensagem.");
        mensagemService.salvar(mensagem);
    }
}
