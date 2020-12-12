package com.nickmafra.demo.tasks;

import com.nickmafra.demo.ApplicationProperties_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTask {

    @Scheduled(fixedDelayString = "${" + ApplicationProperties_.NICKMAFRA_TASKS_HELLOTASK_FIXEDDELAY + "}")
    public void execute() {
        log.info("Hello!");
    }
}
