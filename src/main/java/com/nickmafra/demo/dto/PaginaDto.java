package com.nickmafra.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginaDto<T> {

    private List<T> conteudo;
    private int numeroPagina;
    private int tamanhoPagina;
    private int totalPaginas;
    private long totalElementos;

    public PaginaDto(Page<T> page) {
        this.conteudo = page.getContent();
        this.numeroPagina = page.getNumber();
        this.tamanhoPagina = page.getSize();
        this.totalPaginas = page.getTotalPages();
        this.totalElementos = page.getTotalElements();
    }
}
