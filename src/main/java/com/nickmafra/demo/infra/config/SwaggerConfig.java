package com.nickmafra.demo.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    protected static final String[] SWAGGER_PATTERNS = {
            "/swagger-ui.html",
            "/webjars/springfox-swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs/**"
    };

    private static final String AUTH_NAME = "Token";
    private static final AuthorizationScope[] AUTH_SCOPE_NONE = {};

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(securitySchemes())
                .select().paths(PathSelectors.any()).build();
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
