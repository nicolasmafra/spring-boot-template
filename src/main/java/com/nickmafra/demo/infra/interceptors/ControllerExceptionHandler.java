package com.nickmafra.demo.infra.interceptors;

import com.nickmafra.demo.Messages_;
import com.nickmafra.demo.dto.ErroDto;
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
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.stream.Collectors;

/**
 * Manipula exceptions lançadas por RestControllers.
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localeResolver;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDto> handleException(Exception e, HttpServletRequest req) {
        ErroDto dto = parseMensagens(req, ErroDto.criar(Messages_.ERRO_GENERICO_INTERNO, e));
        log.error(dto.getMensagem(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErroDto> handleBadRequestException(BadRequestException e, HttpServletRequest req) {
        ErroDto dto = parseMensagens(req, ErroDto.criar(e, Messages_.ERRO_GENERICO_CLIENTE, e.getCause()));
        log.debug(dto.getMensagem(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroDto> handleHttpMessageNotReadableException(Exception e, HttpServletRequest req) {
        ErroDto dto = parseMensagens(req, ErroDto.criar(Messages_.CONTEUDO_NAO_LEGIVEL, e));
        log.debug(dto.getMensagem(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest req) {
        String mensagem = parseMensagem(req, Messages_.CAMPOS_INVALIDOS_PREFIXO) + ": " + criarMensagemBindResult(e.getBindingResult());
        ErroDto dto = ErroDto.criar(mensagem, e);
        log.debug(dto.getMensagem(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler({ NaoEncontradoException.class })
    public ResponseEntity<ErroDto> handleNaoEncontradoException(NaoEncontradoException e, HttpServletRequest req) {
        ErroDto dto = parseMensagens(req, ErroDto.criar(e, Messages_.ERRO_GENERICO_NAO_ENCONTRADO));
        log.debug(dto.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler({ JaCadastradoException.class })
    public ResponseEntity<ErroDto> handleJaCadastradoException(JaCadastradoException e, HttpServletRequest req) {
        ErroDto dto = parseMensagens(req, ErroDto.criar(e, Messages_.ERRO_GENERICO_JA_CADASTRADO));
        log.debug(dto.getMensagem());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    private String parseMensagem(HttpServletRequest req, String msgCode) {
        if (msgCode == null)
            return null;
        if (msgCode.length() > 100 || msgCode.contains(" ") || !msgCode.contains("."))
            return msgCode; // não é código de mensagem

        try {
            return messageSource.getMessage(msgCode, null, localeResolver.resolveLocale(req));
        } catch (NoSuchMessageException e) {
            log.debug("Código de mensagem não encontrado: {}", msgCode);
            return msgCode;
        }
    }

    private ErroDto parseMensagens(HttpServletRequest req, ErroDto dto) {
        dto.setMensagem(parseMensagem(req, dto.getMensagem()));
        dto.setDetalhes(parseMensagem(req, dto.getDetalhes()));
        return dto;
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
