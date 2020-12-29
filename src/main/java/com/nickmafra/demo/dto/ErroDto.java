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
}
