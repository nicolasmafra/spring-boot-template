package com.nickmafra.demo.infra.exception;

public class NaoEncontradoException extends AppRuntimeException {

    public NaoEncontradoException() {
    }

    public NaoEncontradoException(String message) {
        super(message);
    }

    public NaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NaoEncontradoException(Throwable cause) {
        super(cause);
    }
}
