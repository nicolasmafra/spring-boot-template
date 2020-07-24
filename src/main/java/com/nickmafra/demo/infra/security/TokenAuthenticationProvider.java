package com.nickmafra.demo.infra.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Objects;

/**
 * Valida um {@link AuthenticationToken}.
 */
@Slf4j
public class TokenAuthenticationProvider implements AuthenticationProvider {

    public static final String TOKEN_SECRETO_MOCK = "xxx999";

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (!(authentication instanceof AuthenticationToken)) {
            return null; // prossegue
        }
        log.info("Validando autenticação via token");

        AuthenticationToken token = (AuthenticationToken) authentication;
        if (!Objects.equals(token.getStrToken(), TOKEN_SECRETO_MOCK)) {
            throw new BadCredentialsException("Token inválido.");
        }
        return new AuthenticationToken(Collections.emptyList(), token.getStrToken(), token.getStrToken(), "admin");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AuthenticationToken.class.isAssignableFrom(aClass);
    }
}
