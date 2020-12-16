package com.nickmafra.demo.infra.startup;

import com.nickmafra.demo.infra.properties.MetadataProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class MetadataStartupRunner {

    @Autowired
    private MetadataProperties metadataProperties;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        if (!StringUtils.isEmpty(metadataProperties.getNomeAutor())) {
            log.info("Criado por: {}", metadataProperties.getNomeAutor());
        }
    }
}
