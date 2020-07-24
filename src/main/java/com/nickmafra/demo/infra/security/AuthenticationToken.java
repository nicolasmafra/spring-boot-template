package com.nickmafra.demo.infra.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

/**
 * Representa uma autenticação via token.
 */
@EqualsAndHashCode(callSuper = false)
public class AuthenticationToken extends PreAuthenticatedAuthenticationToken {

    @Getter
    private final String strToken;

    public AuthenticationToken(String strToken) {
        super(null, null);
        this.strToken = strToken;
    }

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, String strToken, String credentials, String principal) {
        super(principal, credentials, authorities);
        this.strToken = strToken;
    }
}
