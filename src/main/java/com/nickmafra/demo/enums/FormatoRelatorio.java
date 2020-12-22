package com.nickmafra.demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FormatoRelatorio {
    PDF("pdf"),
    XLSX("xlsx");

    private final String extensao;

    public String comExtensao(String fileBaseName) {
        return fileBaseName + "." + extensao;
    }
}
