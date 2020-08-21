package com.nickmafra.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface DateService {
    ZonedDateTime zonedAgora();

    LocalDateTime agora();

    LocalDate hoje();
}
