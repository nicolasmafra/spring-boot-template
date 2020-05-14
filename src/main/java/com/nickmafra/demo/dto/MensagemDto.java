package com.nickmafra.demo.dto;

import com.nickmafra.demo.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensagemDto implements Serializable {

    private static final String SEPARADOR = "\n\n";

    @NotEmpty
    private String titulo;
    @NotEmpty
    private String conteudo;

    public MensagemDto(Mensagem mensagem) {
        this.titulo = mensagem.getTitulo();
        this.conteudo = mensagem.getConteudo();
    }

    public Mensagem toMensagem() {
        return new Mensagem(titulo, conteudo);
    }

    public String serializar() {
        return titulo + SEPARADOR + conteudo;
    }

    public static MensagemDto deserializar(String string) {
        MensagemDto mensagem = new MensagemDto();
        int i = string.indexOf(SEPARADOR);
        if (i >= 0) {
            mensagem.setTitulo(string.substring(0, i));
            mensagem.setConteudo(string.substring(i));
        } else {
            mensagem.setTitulo(string);
            mensagem.setConteudo("");
        }
        return mensagem;
    }
}
