package com.nickmafra.demo.infra.interceptors;

import com.nickmafra.demo.Messages_;
import com.nickmafra.demo.infra.exception.BadRequestException;
import com.nickmafra.demo.infra.exception.JaCadastradoException;
import com.nickmafra.demo.infra.exception.NaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AppErrorHandler extends DefaultErrorAttributes {

    private static final String MSG1 = "message";
    private static final String MSG2 = "details";

    @Autowired
    private MessageSource messageSource;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest req, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(req, includeStackTrace);
        Integer status = (Integer) errorAttributes.get("status");
        Throwable error = getError(req);

        // handle by status
        if (error == null) {
            if (status == 404) {
                setMessageAttributes(errorAttributes, Messages_.ERRO_URL_INVALIDA);
            } else if (status == 403) {
                setMessageAttributes(errorAttributes, Messages_.ERRO_ACESSO_NEGADO);
            } else if (status == 500) {
                setMessageAttributes(errorAttributes, Messages_.ERRO_GENERICO_INTERNO);
            }
            parseMessageAttributes(errorAttributes, req);
            return errorAttributes;
        }

        // handle by exception

        HttpStatus httpStatus = null;
        if (error instanceof JaCadastradoException) {
            httpStatus = handleJaCadastradoException(errorAttributes, req, (JaCadastradoException) error);
        } else if (error instanceof NaoEncontradoException) {
            httpStatus = handleNaoEncontradoException(errorAttributes, req, (NaoEncontradoException) error);
        } else if (error instanceof MethodArgumentNotValidException) {
            httpStatus = handleMethodArgumentNotValidException(errorAttributes, req, (MethodArgumentNotValidException) error);
        } else if (error instanceof HttpMessageNotReadableException) {
            httpStatus = handleHttpMessageNotReadableException(errorAttributes, req, (HttpMessageNotReadableException) error);
        } else if (error instanceof BadRequestException) {
            httpStatus = handleBadRequestException(errorAttributes, req, (BadRequestException) error);
        } else if (error instanceof Exception) {
            httpStatus = handleException(errorAttributes, req, (Exception) error);
        }

        String message = (String) errorAttributes.get(MSG1);
        if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error(message, error);
        } else {
            log.debug(message, error);
        }
        return errorAttributes;
    }

    public HttpStatus handleException(Map<String, Object> errorAttributes, WebRequest req, Exception e) {
        setMessageAttributes(errorAttributes, Messages_.ERRO_GENERICO_INTERNO, e);
        parseMessageAttributes(errorAttributes, req);
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpStatus handleBadRequestException(Map<String, Object> errorAttributes, WebRequest req, BadRequestException e) {
        setMessageAttributes(errorAttributes, e, Messages_.ERRO_GENERICO_CLIENTE, e.getCause());
        parseMessageAttributes(errorAttributes, req);
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus handleHttpMessageNotReadableException(Map<String, Object> errorAttributes, WebRequest req, HttpMessageNotReadableException e) {
        setMessageAttributes(errorAttributes, Messages_.ERRO_CONTEUDO_NAO_LEGIVEL, e);
        parseMessageAttributes(errorAttributes, req);
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus handleMethodArgumentNotValidException(Map<String, Object> errorAttributes, WebRequest req, MethodArgumentNotValidException e) {
        String mensagem = getParsedMessage(req, Messages_.ERRO_CAMPOS_INVALIDOS_PREFIXO) + ": " + parseBindResult(e.getBindingResult());
        setMessageAttributes(errorAttributes, mensagem, e);
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus handleNaoEncontradoException(Map<String, Object> errorAttributes, WebRequest req, NaoEncontradoException e) {
        setMessageAttributes(errorAttributes, e, Messages_.ERRO_GENERICO_NAO_ENCONTRADO);
        parseMessageAttributes(errorAttributes, req);
        return HttpStatus.NOT_FOUND;
    }

    public HttpStatus handleJaCadastradoException(Map<String, Object> errorAttributes, WebRequest req, JaCadastradoException e) {
        setMessageAttributes(errorAttributes, e, Messages_.ERRO_GENERICO_JA_CADASTRADO);
        parseMessageAttributes(errorAttributes, req);
        return HttpStatus.CONFLICT;
    }

    public static void setMessageAttributes(Map<String, Object> errorAttributes, Object... args) {
        String message = null;
        String details = null;
        for (Object arg : args) {
            if (arg instanceof Throwable) {
                arg = ((Throwable) arg).getMessage();
            }
            if (arg != null) {
                if (message == null)
                    message = arg.toString();
                else if (details == null)
                    details = arg.toString();
            }
        }
        errorAttributes.put(MSG1, message);
        errorAttributes.put(MSG2, details);
    }

    private void parseMessageAttributes(Map<String, Object> errorAttributes, WebRequest req) {
        parseAttribute(errorAttributes, req, MSG1);
        parseAttribute(errorAttributes, req, MSG2);
    }

    private void parseAttribute(Map<String, Object> errorAttributes, WebRequest req, String attributeName) {
        errorAttributes.put(attributeName, getParsedMessage(req, (String) errorAttributes.get(attributeName)));
    }

    private String getParsedMessage(WebRequest req, String msgCode) {
        if (msgCode == null)
            return null;
        if (msgCode.length() > 100 || msgCode.contains(" ") || !msgCode.contains("."))
            return msgCode; // não é código de mensagem

        try {
            return messageSource.getMessage(msgCode, null, req.getLocale());
        } catch (NoSuchMessageException e) {
            log.debug("Código de mensagem não encontrado: {}", msgCode);
            return msgCode;
        }
    }

    private String parseBindResult(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(this::criarMensagemFieldError)
                .collect(Collectors.joining("; "));
    }

    private String criarMensagemFieldError(FieldError fieldError) {
        return MessageFormat.format("[{0}] {1}", fieldError.getField(), fieldError.getDefaultMessage());
    }

}
