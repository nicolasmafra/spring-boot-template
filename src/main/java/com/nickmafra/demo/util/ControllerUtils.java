package com.nickmafra.demo.util;

import com.nickmafra.demo.dto.ArquivoDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.activation.MimetypesFileTypeMap;

public class ControllerUtils {

    private ControllerUtils() {}

    public static ResponseEntity<byte[]> toResponse(ArquivoDto arquivoDto) {
        if (arquivoDto.getConteudo() == null || arquivoDto.getConteudo().length == 0) {
            return ResponseEntity.noContent().build();
        }
        String contentType = new MimetypesFileTypeMap().getContentType(arquivoDto.getNomeArquivo());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + arquivoDto.getNomeArquivo() + "\"")
                .body(arquivoDto.getConteudo());
    }
}
