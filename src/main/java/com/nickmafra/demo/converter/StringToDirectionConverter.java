package com.nickmafra.demo.converter;

import org.springframework.data.domain.Sort;

public class StringToDirectionConverter extends StringToEnumConverter<Sort.Direction> {

    public StringToDirectionConverter() {
        super(Sort.Direction.class);
    }
}
