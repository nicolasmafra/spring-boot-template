package com.nickmafra.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface DateService {
    ZoneId zone();

    LocalDateTime agora();

    LocalDate hoje();
}
