package com.nickmafra.demo.infra.exception;

public class JaCadastradoException extends AppRuntimeException {

    public JaCadastradoException() {
    }

    public JaCadastradoException(String message) {
        super(message);
    }

    public JaCadastradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public JaCadastradoException(Throwable cause) {
        super(cause);
    }
}
