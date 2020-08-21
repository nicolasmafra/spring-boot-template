package com.nickmafra.demo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service
public class DateServiceImpl implements DateService {

    @Override
    public ZonedDateTime zonedAgora() {
        return ZonedDateTime.now();
    }

    @Override
    public LocalDateTime agora() {
        return zonedAgora().toLocalDateTime();
    }

    @Override
    public LocalDate hoje() {
        return zonedAgora().toLocalDate();
    }

}
