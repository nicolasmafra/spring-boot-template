package com.nickmafra.demo.util;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.PaginaDto;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class MockUtils {

    private MockUtils() {}

    public static <T> List<T> gerarItens(int offset, int tamanho, IntFunction<T> supplier) {
        if (tamanho <= 0) {
            return Collections.emptyList();
        }
        List<T> lista = new ArrayList<>(tamanho);
        for (int i = 0; i < tamanho; i++) {
            lista.add(supplier.apply(offset + i));
        }
        return lista;
    }

    public static <T> List<T> gerarItens(int tamanhoTotal, IntFunction<T> supplier) {
        return gerarItens(0, tamanhoTotal, supplier);
    }

    @SuppressWarnings("unchecked")
    public static <T, U> Function<T, U> getAccessor(String name, Class<?> clazz) {
        String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        Method method;
        try {
            method = clazz.getDeclaredMethod(getterName);
        } catch (Exception e) {
            throw new MockUtilsException(e);
        }
        return item -> {
            try {
                return (U) method.invoke(item);
            } catch (Exception e) {
                throw new MockUtilsException(e);
            }
        };
    }

    public static <T> List<T> ordenar(ConsultaDto consultaDto, List<T> itens) {
        if (itens.isEmpty())
            return itens;

        if (!StringUtils.isEmpty(consultaDto.getOrdenacao())) {
            Function<T, Comparable<?>> accessor = getAccessor(consultaDto.getOrdenacao(), itens.get(0).getClass());
            itens = itens.stream().sorted(Comparator.comparing(accessor, new GenericComparator<>()))
                    .collect(Collectors.toList());
        }
        if (consultaDto.getDirecao() == Sort.Direction.DESC) {
            Collections.reverse(itens);
        }
        return itens;
    }

    public static <T> List<T> fatiar(List<T> itens, int offset, int tamanho) {
        return itens.stream().skip(offset).limit(tamanho).collect(Collectors.toList());
    }

    public static <T> PaginaDto<T> pegarPagina(ConsultaDto consultaDto, List<T> itens) {
        int pagina = Math.max(consultaDto.getPagina() - ConsultaDto.FIRST_PAGE, 0);
        int tamanho = consultaDto.getTamanho();
        int offset = pagina * tamanho;
        List<T> conteudo = fatiar(itens, offset, tamanho);
        return new PaginaDto<>(consultaDto, itens.size(), conteudo);
    }

    private static long makeSeed(Class<?> clazz, long n, long variante) {
        return (variante * 10000L + n) * Integer.MAX_VALUE + clazz.hashCode();
    }

    @SafeVarargs
    public static <T> T randomize(int n, long variante, T... valores) {
        long seed = makeSeed(valores[0].getClass(), n, variante);
        return getRandomIndex(seed, valores);
    }

    @SafeVarargs
    public static <T> T randomize(int n, T... valores) {
        long seed = makeSeed(valores[0].getClass(), n, Arrays.hashCode(valores));
        return getRandomIndex(seed, valores);
    }

    private static final Random RANDOM = new Random();

    private static synchronized <T> T getRandomIndex(long seed, T[] valores) {
        RANDOM.setSeed(seed);
        int index = RANDOM.nextInt(valores.length);
        return valores[index];
    }

    public static class GenericComparator<T> implements Comparator<T> {

        @SuppressWarnings("unchecked")
        @Override
        public int compare(T o1, T o2) {
            if (o1 == o2) {
                return 0;
            }
            if (o1 == null || o2 == null) {
                return o1 == null ? 1 : -1; // nulls last
            }
            if (Comparable.class.isAssignableFrom(o1.getClass())) {
                return ((Comparable<T>) o1).compareTo(o2);
            }
            return 0;
        }
    }

    public static class MockUtilsException extends RuntimeException {
        public MockUtilsException(Throwable cause) {
            super(cause);
        }
    }
}
