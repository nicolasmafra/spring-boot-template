package com.nickmafra.demo.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ConsultaDto {

    public static final int FIRST_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;

    private int pagina = FIRST_PAGE;
    private int tamanho = DEFAULT_PAGE_SIZE;
    private List<String> ordenacao;

    private static Sort.Order toOrder(String singleSort) {
        String[] pedacos = singleSort.split(" ");
        String property = pedacos[0];
        Sort.Direction direction;
        if (pedacos.length >= 2) {
            direction = Sort.Direction.fromString(pedacos[1]);
        } else {
            direction = Sort.DEFAULT_DIRECTION;
        }
        return new Sort.Order(direction, property);
    }

    public Sort toSort() {
        if (StringUtils.isEmpty(ordenacao)) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = ordenacao.stream().map(ConsultaDto::toOrder).collect(Collectors.toList());
        return Sort.by(orders);
    }

    public Pageable toPageable(Sort sort) {
        return PageRequest.of(pagina, tamanho, sort);
    }

    public Pageable toPageable() {
        return toPageable(toSort());
    }
}
