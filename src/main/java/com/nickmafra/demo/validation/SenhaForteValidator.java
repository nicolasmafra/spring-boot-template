package com.nickmafra.demo.validation;

import com.nickmafra.demo.Messages_;
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
        TAMANHO8(".{8,}", "{" + Messages_.SENHA_FORTE_REGRAS_TAMANHO8 + "}"),
        MAIUSCULA(".*[A-Z].*", "{" + Messages_.SENHA_FORTE_REGRAS_MAIUSCULA + "}"),
        MINUSCULA(".*[a-z].*", "{" + Messages_.SENHA_FORTE_REGRAS_MINUSCULA + "}"),
        DIGITO(".*[0-9].*", "{" + Messages_.SENHA_FORTE_REGRAS_DIGITO + "}"),
        CHAR_ESPECIAL(".*[^A-Za-z0-9].*", "{" + Messages_.SENHA_FORTE_REGRAS_CHAR_ESPECIAL + "}"),
        NAO_BRANCOS("[^\\s]*", "{" + Messages_.SENHA_FORTE_REGRAS_NAO_BRANCO + "}");

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
        String mensagem = "{" + Messages_.SENHA_FORTE_REGRAS + "}: " + textoItens + ".";
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(mensagem).addConstraintViolation();
        return false;
    }

}
