package com.nickmafra.demo.util;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.PaginaDto;

import java.util.*;
import java.util.function.IntFunction;

public class MockUtils {

    private MockUtils() {}

    public static <T> List<T> replicarItem(int offset, int tamanho, IntFunction<T> supplier) {
        if (tamanho <= 0) {
            return Collections.emptyList();
        }
        List<T> lista = new ArrayList<>(tamanho);
        for (int i = 0; i < tamanho; i++) {
            lista.add(supplier.apply(offset + i));
        }
        return lista;
    }

    public static <T> PaginaDto<T> criarPagina(ConsultaDto consultaDto, int totalElementos, IntFunction<T> supplier) {
        int pagina = Math.max(consultaDto.getPagina(), 0);
        consultaDto.setPagina(pagina);
        int tamanhoMaximo = consultaDto.getTamanho();
        int offset = pagina * tamanhoMaximo;
        int offsetEnd = Math.min(offset + tamanhoMaximo, totalElementos); // end exclusivo
        int tamanho = offsetEnd - offset;
        return new PaginaDto<>(consultaDto, totalElementos, replicarItem(offset, tamanho, supplier));
    }

    private static long makeSeed(Class<?> clazz, long n, long variante) {
        return (variante * 10000L + n) * Integer.MAX_VALUE + clazz.hashCode();
    }

    public static <T> T randomize(int n, long variante, T... valores) {
        long seed = makeSeed(valores[0].getClass(), n, variante);
        return getRandomIndex(seed, valores);
    }

    public static <T> T randomize(int n, T... valores) {
        long seed = makeSeed(valores[0].getClass(), n, Arrays.hashCode(valores));
        return getRandomIndex(seed, valores);
    }

    private static final Random RANDOM = new Random();

    private static synchronized <T> T getRandomIndex(long seed, T... valores) {
        RANDOM.setSeed(seed);
        int index = RANDOM.nextInt(valores.length);
        return valores[index];
    }
}
