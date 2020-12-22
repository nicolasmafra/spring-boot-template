package com.nickmafra.demo.dto;

import com.nickmafra.demo.enums.FormatoRelatorio;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FiltroRelatorioUsuarioDto {

    @NotNull
    private FormatoRelatorio formato;
    private String nomeLike;
}
