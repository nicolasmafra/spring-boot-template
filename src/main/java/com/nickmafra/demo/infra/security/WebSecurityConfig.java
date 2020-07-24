package com.nickmafra.demo.infra.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_PATTERNS = {
            "/swagger-ui.html",
            "/webjars/springfox-swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs/**"
    };

    private static final RequestMatcher[] PUBLIC_REQ_MATCHERS = {
            new AntPathRequestMatcher("/api/usuarios", HttpMethod.POST.toString()),
            new AntPathRequestMatcher("/login", HttpMethod.POST.toString()),
            new AntPathRequestMatcher("/api/public/**")
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(SWAGGER_PATTERNS).permitAll()
                .requestMatchers(PUBLIC_REQ_MATCHERS).permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
                .and()
            .addFilterBefore(new TokenAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new TokenAuthenticationProvider());
    }

}
