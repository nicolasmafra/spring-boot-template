package com.nickmafra.demo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class DateServiceImpl implements DateService {

    @Override
    public ZoneId zone() {
        return ZoneId.systemDefault();
    }

    @Override
    public LocalDateTime agora() {
        return LocalDateTime.now(zone());
    }

    @Override
    public LocalDate hoje() {
        return LocalDate.now(zone());
    }

}
