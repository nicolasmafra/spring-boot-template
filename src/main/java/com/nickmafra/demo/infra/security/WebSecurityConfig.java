package com.nickmafra.demo.infra.security;

import com.nickmafra.demo.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
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

    @Autowired
    private JwtService jwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(SWAGGER_PATTERNS).permitAll()
                .requestMatchers(PUBLIC_REQ_MATCHERS).permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().denyAll();
        http.addFilterBefore(new JwtAuthenticationFilter(jwtService, "/api/**"), UsernamePasswordAuthenticationFilter.class);
    }
}
