package com.nickmafra.demo.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class PaginaDto<T> {

    private List<T> conteudo;
    private int numeroPagina;
    private int tamanhoPagina;
    private int totalPaginas;
    private long totalElementos;

    private PaginaDto() {}

    public PaginaDto(Page<T> page) {
        this.conteudo = page.getContent();
        this.numeroPagina = page.getNumber();
        this.tamanhoPagina = page.getSize();
        this.totalPaginas = page.getTotalPages();
        this.totalElementos = page.getTotalElements();
    }

    public <U> PaginaDto(PaginaDto<U> pagina, Function<U, T> converter) {
        this.conteudo = pagina.conteudo.stream().map(converter).collect(Collectors.toList());
        this.numeroPagina = pagina.numeroPagina;
        this.tamanhoPagina = pagina.tamanhoPagina;
        this.totalPaginas = pagina.totalPaginas;
        this.totalElementos = pagina.totalElementos;
    }

    public <T2> PaginaDto<T2> map(Function<T, T2> converter) {
        return new PaginaDto<>(this, converter);
    }
}
