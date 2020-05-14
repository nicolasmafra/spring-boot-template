package com.nickmafra.demo.infra.jms;

import com.nickmafra.demo.dto.MensagemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class EnviadorFila {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void enviarMensagem(MensagemDto mensagem) {
        jmsTemplate.convertAndSend(JmsConstants.DESTINATION, mensagem.serializar());
    }
}
