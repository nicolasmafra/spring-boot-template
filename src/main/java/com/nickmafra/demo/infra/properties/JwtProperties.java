package com.nickmafra.demo.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "nickmafra.jwt")
public class JwtProperties {

    private String issuer;
    private Duration duration;

}
