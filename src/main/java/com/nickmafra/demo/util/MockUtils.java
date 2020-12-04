package com.nickmafra.demo.util;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.PaginaDto;

import java.util.ArrayList;
import java.util.List;

public class MockUtils {

    private MockUtils() {}

    public static <T> List<T> replicarItem(T item, int tamanho) {
        List<T> lista = new ArrayList<>(tamanho);
        for (int i = 0; i < tamanho; i++) {
            lista.add(item);
        }
        return lista;
    }

    public static <T> PaginaDto<T> criarPagina(ConsultaDto consultaDto, int totalElementos, T item) {
        int tamanhoMaximo = consultaDto.getTamanho();
        int offset = consultaDto.getPagina() * tamanhoMaximo;
        int offsetEnd = Math.min(offset + tamanhoMaximo, totalElementos + 1); // end exclusivo
        int tamanho = offsetEnd - offset;
        return new PaginaDto<>(consultaDto, totalElementos, replicarItem(item, tamanho));
    }
}
