package com.nickmafra.demo.infra.advice;

import com.nickmafra.demo.Messages_;
import com.nickmafra.demo.dto.ErroDto;
import com.nickmafra.demo.infra.exception.AppAuthenticationException;
import com.nickmafra.demo.infra.exception.AppRuntimeException;
import com.nickmafra.demo.infra.exception.BadRequestException;
import com.nickmafra.demo.infra.exception.JaCadastradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.stream.Collectors;

/**
 * Manipula exceptions lançadas por RestControllers.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localeResolver;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDto> handleException(Exception e) {
        ErroDto dto = new ErroDto(e);
        log.error(AppRuntimeException.MSG_ERRO_INTERNO, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    @ExceptionHandler({BadRequestException.class, AppAuthenticationException.class})
    public ResponseEntity<ErroDto> handleBadRequestException(Exception e) {
        ErroDto dto = new ErroDto(e);
        log.debug(BadRequestException.MSG_GENERICA, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<ErroDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return handleBadRequestException(new BadRequestException(BadRequestException.MSG_GENERICA, e));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ErroDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest req) {
        String prefixo = messageSource.getMessage(Messages_.CAMPOS_INVALIDOS_PREFIXO, null, localeResolver.resolveLocale(req));
        String mensagem = prefixo + criarMensagemBindResult(e.getBindingResult());
        return handleBadRequestException(new BadRequestException(mensagem, e));
    }

    @ExceptionHandler({ JaCadastradoException.class })
    public ResponseEntity<ErroDto> handleJaCadastradoException(JaCadastradoException e) {
        ErroDto dto = new ErroDto(e);
        log.debug(dto.getMensagem());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    private String criarMensagemBindResult(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(this::criarMensagemFieldError)
                .collect(Collectors.joining("; "));
    }

    private String criarMensagemFieldError(FieldError fieldError) {
        return MessageFormat.format("[{0}] {1}", fieldError.getField(), fieldError.getDefaultMessage());
    }

}
