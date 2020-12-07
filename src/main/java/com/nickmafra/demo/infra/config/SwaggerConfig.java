package com.nickmafra.demo.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String AUTH_NAME = "Token";
    private static final AuthorizationScope[] AUTH_SCOPE_NONE = {};
    private static final List<String> PATTERNS = Arrays.asList("/api/**", "/login");

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(securitySchemes())
                .select().paths(this::pathMatches)
                .build();
    }

    private boolean pathMatches(String path) {
        return PATTERNS.stream().anyMatch(pattern -> new AntPathMatcher().match(pattern, path));
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(securityReferences())
                .forPaths(PathSelectors.ant("/api/**"))
                .build();
    }

    private List<SecurityReference> securityReferences() {
        return Collections.singletonList(
                new SecurityReference(AUTH_NAME, AUTH_SCOPE_NONE)
        );
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(
                new ApiKey(AUTH_NAME, "Authorization", "header")
        );
    }
}
