package com.nickmafra.demo.validation;

import com.nickmafra.demo.validation.annotation.SenhaForte;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SenhaForteValidator implements ConstraintValidator<SenhaForte, String> {

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public enum ItemValidacao {
        TAMANHO8(".{8,}", "{senha.forte.regras.tamanho8}"),
        MAIUSCULA(".*[A-Z].*", "{senha.forte.regras.maiuscula}"),
        MINUSCULA(".*[a-z].*", "{senha.forte.regras.minuscula}"),
        DIGITO(".*[0-9].*", "{senha.forte.regras.digito}"),
        CHAR_ESPECIAL(".*[^A-Za-z0-9].*", "{senha.forte.regras.char-especial}"),
        NAO_BRANCOS("[^\\s]*", "{senha.forte.regras.nao.branco}");

        String regex;
        String mensagem;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // não valida neste caso
        }
        String textoItens = Arrays.stream(ItemValidacao.values())
                .filter(item -> !value.matches(item.getRegex()))
                .map(ItemValidacao::getMensagem)
                .collect(Collectors.joining(", "));
        if (textoItens.isEmpty()) {
            return true;
        }
        String mensagem = "{senha.forte.regras}: " + textoItens + ".";
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(mensagem).addConstraintViolation();
        return false;
    }

}
