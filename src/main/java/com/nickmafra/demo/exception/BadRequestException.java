package com.nickmafra.demo.exception;

public class BadRequestException extends AppRuntimeException {

    public static final String MSG_GENERICA = "Requisição inválida.";
    public static final String MSG_ID_NAO_ENCONTRADO = "Não foi encontrado registro com o ID informado.";

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
