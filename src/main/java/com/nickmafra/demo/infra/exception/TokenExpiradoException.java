package com.nickmafra.demo.infra.exception;

public class TokenExpiradoException extends Exception {

    public TokenExpiradoException() {
    }

    public TokenExpiradoException(String message) {
        super(message);
    }

    public TokenExpiradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpiradoException(Throwable cause) {
        super(cause);
    }
}
