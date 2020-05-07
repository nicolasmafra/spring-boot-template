package com.nickmafra.demo.infra.advice;

import com.nickmafra.demo.dto.ErroDto;
import com.nickmafra.demo.infra.exception.BadRequestException;
import com.nickmafra.demo.infra.exception.JaCadastradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDto> handleException(Exception e) {
        ErroDto dto = new ErroDto(e);
        log.error(dto.getMensagem(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErroDto> handleBadRequestException(Exception e) {
        ErroDto dto = new ErroDto(e);
        log.debug(dto.getMensagem(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<ErroDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return handleBadRequestException(new BadRequestException(BadRequestException.MSG_GENERICA, e));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ErroDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String mensagem = "Campos inválidos: " + criarMensagemBindResult(e.getBindingResult());
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
