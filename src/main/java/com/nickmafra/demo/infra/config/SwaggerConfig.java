package com.nickmafra.demo.infra.config;

import com.nickmafra.demo.Messages_;
import com.nickmafra.demo.infra.properties.MetadataProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
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
    public Docket api(
            @Autowired(required = false) BuildProperties buildProperties,
            MetadataProperties metadataProperties,
            MessageSource messageSource) {

        String buildDescription = buildProperties != null ? (makeBuildDescription(buildProperties)) : "";
        String description = messageSource.getMessage(Messages_.METADATA_PROJECT_DESCRIPTION, new Object[] { metadataProperties.getGitUrl() }, MessageConfig.DEFAULT_LOCALE)
                + buildDescription;

        ApiInfo apiInfo = new ApiInfo(metadataProperties.getNomeApi(), description, metadataProperties.getVersaoApi(),
                null, null, null, null, Collections.emptySet()
        );
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .securitySchemes(securitySchemes())
                .securityContexts(Collections.singletonList(securityContext()))
                .select().paths(this::pathMatches)
                .build();
    }

    private String makeBuildDescription(BuildProperties buildProperties) {
        return "\n\nInformações do projeto:"
                + "\nVersão: " + buildProperties.getVersion()
                + "\nData da compilação: " + buildProperties.getTime();
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
