package com.nickmafra.demo.infra.startup;

import com.nickmafra.demo.infra.properties.MetadataConfigProperties;
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
    private MetadataConfigProperties metadataConfigProperties;

    @Override
    public void run(ApplicationArguments args) {
        if (!StringUtils.isEmpty(metadataConfigProperties.getNomeAutor())) {
            log.info("Criado por: {}", metadataConfigProperties.getNomeAutor());
        }
    }
}
