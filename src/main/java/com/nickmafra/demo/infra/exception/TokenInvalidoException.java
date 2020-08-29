package com.nickmafra.demo.infra.exception;

public class TokenInvalidoException extends Exception {

    public TokenInvalidoException() {
    }

    public TokenInvalidoException(String message) {
        super(message);
    }

    public TokenInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenInvalidoException(Throwable cause) {
        super(cause);
    }
}
