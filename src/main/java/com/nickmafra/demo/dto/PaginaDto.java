package com.nickmafra.demo.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Data
public class PaginaDto<T> {

    private Page<T> page;
    private List<T> conteudo;
    private int numeroPagina;
    private int tamanhoPagina;
    private int totalPaginas;
    private long totalElementos;

    public PaginaDto(Page<T> page) {
        this.page = page;
        this.conteudo = page.getContent();
        this.numeroPagina = page.getNumber();
        this.tamanhoPagina = page.getSize();
        this.totalPaginas = page.getTotalPages();
        this.totalElementos = page.getTotalElements();
    }

    public <U> PaginaDto<U> map(Function<T, U> converter) {
        return new PaginaDto<>(page.map(converter));
    }
}
