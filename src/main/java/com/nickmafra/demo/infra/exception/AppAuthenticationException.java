package com.nickmafra.demo.infra.exception;

import org.springframework.security.core.AuthenticationException;

public class AppAuthenticationException extends AuthenticationException {
    public AppAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AppAuthenticationException(String msg) {
        super(msg);
    }
}
