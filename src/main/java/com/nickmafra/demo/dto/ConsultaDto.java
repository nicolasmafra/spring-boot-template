package com.nickmafra.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConsultaDto {

    public static final int FIRST_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_ORDER_PROPERTY = "id";

    private int pagina = FIRST_PAGE;
    private int tamanho = DEFAULT_PAGE_SIZE;
    private String ordenacao;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
    private Sort.Direction direcao;

    public void setToFullResult() {
        pagina = FIRST_PAGE;
        tamanho = DEFAULT_PAGE_SIZE;
    }

    public Sort toSort() {
        List<Sort.Order> orders = new ArrayList<>();
        if (!StringUtils.isEmpty(ordenacao) && !ordenacao.equalsIgnoreCase(DEFAULT_ORDER_PROPERTY)) {
            orders.add(new Sort.Order(direcao, ordenacao));
        }
        orders.add(new Sort.Order(direcao, DEFAULT_ORDER_PROPERTY));
        return Sort.by(orders);
    }

    public Pageable toPageable(Sort sort) {
        return PageRequest.of(pagina - FIRST_PAGE, tamanho, sort);
    }

    public Pageable toPageable() {
        return toPageable(toSort());
    }
}
