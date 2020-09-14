package com.nickmafra.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoDto {
    private byte[] conteudo;
    private String nomeArquivo;
}
