package com.nickmafra.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nickmafra.demo.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensagemDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotEmpty
    private String titulo;
    @NotEmpty
    private String conteudo;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataEnvio;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataRecebimento;

    public MensagemDto(Mensagem mensagem) {
        this.id = mensagem.getId();
        this.titulo = mensagem.getTitulo();
        this.conteudo = mensagem.getConteudo();
        this.dataEnvio = mensagem.getDataEnvio();
        this.dataRecebimento = mensagem.getDataRecebimento();
    }

    public Mensagem toMensagem() {
        Mensagem mensagem = new Mensagem();
        mensagem.setTitulo(titulo);
        mensagem.setConteudo(conteudo);
        return mensagem;
    }
}
