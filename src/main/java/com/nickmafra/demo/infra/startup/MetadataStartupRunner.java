package com.nickmafra.demo.infra.startup;

import com.nickmafra.demo.infra.properties.MetadataProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class MetadataStartupRunner implements ApplicationRunner {

    @Autowired
    private MetadataProperties metadataProperties;

    @Override
    public void run(ApplicationArguments args) {
        if (!StringUtils.isEmpty(metadataProperties.getNomeAutor())) {
            log.info("Criado por: {}", metadataProperties.getNomeAutor());
        }
    }
}
