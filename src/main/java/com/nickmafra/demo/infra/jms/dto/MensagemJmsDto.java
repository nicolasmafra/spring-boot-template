package com.nickmafra.demo.infra.jms.dto;

import com.nickmafra.demo.model.Mensagem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MensagemJmsDto implements Serializable {

    private String titulo;
    private String conteudo;
    private LocalDateTime dataEnvio;

    public MensagemJmsDto(Mensagem mensagem) {
        this.titulo = mensagem.getTitulo();
        this.conteudo = mensagem.getConteudo();
        this.dataEnvio = mensagem.getDataEnvio();
    }

    public Mensagem toMensagem() {
        Mensagem mensagem = new Mensagem();
        mensagem.setTitulo(titulo);
        mensagem.setConteudo(conteudo);
        mensagem.setDataEnvio(dataEnvio);
        return mensagem;
    }
}
