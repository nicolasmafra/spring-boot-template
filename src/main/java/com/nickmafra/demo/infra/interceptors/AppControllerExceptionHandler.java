package com.nickmafra.demo.infra.interceptors;

import com.nickmafra.demo.Messages_;
import com.nickmafra.demo.infra.exception.BadRequestException;
import com.nickmafra.demo.infra.exception.JaCadastradoException;
import com.nickmafra.demo.infra.exception.NaoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class AppControllerExceptionHandler {

    private static final String MSG1 = "message";
    private static final String MSG2 = "details";

    @Autowired
    private AppErrorAttributes errorAttributes;
    @Autowired
    private MessageSource messageSource;

    private Map<String, Object> getErrorAttributesBody(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, true);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest req, Exception e) {
        Map<String, Object> body = getErrorAttributesBody(req);
        setMessageAttributes(body, Messages_.ERRO_GENERICO_INTERNO, e);
        parseMessageAttributes(body, req);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleRnpBadRequestException(HttpServletRequest req, BadRequestException e) {
        Map<String, Object> body = getErrorAttributesBody(req);
        setMessageAttributes(body, e, Messages_.ERRO_GENERICO_CLIENTE, e.getCause());
        parseMessageAttributes(body, req);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException e) {
        Map<String, Object> body = getErrorAttributesBody(req);
        setMessageAttributes(body, Messages_.ERRO_CONTEUDO_NAO_LEGIVEL, e);
        parseMessageAttributes(body, req);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) {
        Map<String, Object> body = getErrorAttributesBody(req);
        String mensagem = getParsedMessage(req, Messages_.ERRO_CAMPOS_INVALIDOS_PREFIXO) + ": " + parseBindResult(e.getBindingResult());
        setMessageAttributes(body, mensagem, e);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontradoException(HttpServletRequest req, NaoEncontradoException e) {
        Map<String, Object> body = getErrorAttributesBody(req);
        setMessageAttributes(body, e, Messages_.ERRO_GENERICO_NAO_ENCONTRADO);
        parseMessageAttributes(body, req);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> handleJaCadastradoException(HttpServletRequest req, JaCadastradoException e) {
        Map<String, Object> body = getErrorAttributesBody(req);
        setMessageAttributes(body, e, Messages_.ERRO_GENERICO_JA_CADASTRADO);
        parseMessageAttributes(body, req);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    public static void setMessageAttributes(Map<String, Object> body, Object... args) {
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
        body.put(MSG1, message);
        body.put(MSG2, details);
    }

    private void parseMessageAttributes(Map<String, Object> body, HttpServletRequest req) {
        parseAttribute(body, req, MSG1);
        parseAttribute(body, req, MSG2);
    }

    private void parseAttribute(Map<String, Object> body, HttpServletRequest req, String attributeName) {
        body.put(attributeName, getParsedMessage(req, (String) body.get(attributeName)));
    }

    private String getParsedMessage(HttpServletRequest req, String msgCode) {
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
