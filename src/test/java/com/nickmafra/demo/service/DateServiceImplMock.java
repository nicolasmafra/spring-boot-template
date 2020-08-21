package com.nickmafra.demo.service;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@Profile("mockDate")
@Primary
class DateServiceImplMock extends DateServiceImpl {

    public ZonedDateTime zonedDateTime;

    @Override
    public ZonedDateTime zonedAgora() {
        return zonedDateTime != null ? zonedDateTime : super.zonedAgora();
    }
}