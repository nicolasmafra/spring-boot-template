package com.nickmafra.demo.infra.exception;

public class AppRuntimeException extends RuntimeException {

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
