package com.nickmafra.demo.infra.exception;

public class AppRuntimeException extends RuntimeException {

    public static final String MSG_ERRO_INTERNO = "Houve um erro interno.";

    public AppRuntimeException() {
    }

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppRuntimeException(Throwable cause) {
        super(cause);
    }
}
