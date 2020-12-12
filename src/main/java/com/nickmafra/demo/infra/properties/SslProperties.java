package com.nickmafra.demo.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "server.ssl")
public class SslProperties {

    private String keyStore;
    private String keyStoreAlias = "1";
    private String keyStorePassword;
}
