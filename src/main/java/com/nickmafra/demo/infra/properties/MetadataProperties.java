package com.nickmafra.demo.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "nickmafra.metadata")
public class MetadataProperties {

    private String nomeApi;
    private String versaoApi;
    private String nomeAutor;
    private String gitUrl;

}
