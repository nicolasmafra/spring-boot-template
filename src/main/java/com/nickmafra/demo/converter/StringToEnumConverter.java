package com.nickmafra.demo.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class StringToEnumConverter<T extends Enum<T>> implements Converter<String, T> {
    private final Class<T> clazz;
    @Override
    public T convert(String source) {
        return Enum.valueOf(clazz, source.toUpperCase());
    }
}
