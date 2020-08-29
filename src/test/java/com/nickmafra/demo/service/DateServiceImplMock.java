package com.nickmafra.demo.service;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Profile("mockDate")
@Primary
class DateServiceImplMock extends DateServiceImpl {

    private LocalDateTime agora;

    @Override
    public LocalDateTime agora() {
        return agora != null ? agora : super.agora();
    }

    @Override
    public LocalDate hoje() {
        return agora().toLocalDate();
    }

    public void setAgora(LocalDateTime agora) {
        this.agora = agora;
    }

    public void setHoje(LocalDate hoje) {
        this.agora = hoje.atStartOfDay();
    }
}