package com.nickmafra.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nickmafra.demo.exception.AppRuntimeException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErroDto {
    private String mensagem;
    private String detalhes;
    @JsonIgnore
    private Exception exception;

    public ErroDto(Exception e) {
        this.exception = e;
        if (e instanceof AppRuntimeException) {
            this.mensagem = e.getMessage();
            if (e.getCause() != null) {
                this.detalhes = e.getCause().getMessage();
            }
        } else {
            this.mensagem = AppRuntimeException.MSG_ERRO_INTERNO;
            this.detalhes = e.getMessage();
        }
    }
}
