package com.nickmafra.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErroDto {
    private String mensagem;
    private String detalhes;
    @JsonIgnore
    private Throwable throwable;

    public static ErroDto criar(Object... args) {
        ErroDto dto = new ErroDto();
        for (Object arg : args) {
            if (arg instanceof Throwable) {
                Throwable t = (Throwable) arg;
                if (dto.getThrowable() != null) {
                    dto.setThrowable(t);
                }
                arg = t.getMessage();
            }
            if (arg != null) {
                if (dto.getMensagem() == null)
                    dto.setMensagem(arg.toString());
                else if (dto.getDetalhes() == null)
                    dto.setDetalhes(arg.toString());
            }
        }
        return dto;
    }
}
