package com.nickmafra.demo.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTask {

    @Scheduled(fixedDelayString = "${nickmafra.tasks.helloTask.fixedDelay}")
    public void execute() {
        log.info("Hello!");
    }
}
