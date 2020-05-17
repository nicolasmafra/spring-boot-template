package com.nickmafra.demo.validation;

import com.nickmafra.demo.validation.annotation.SenhaForte;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SenhaForteValidator implements ConstraintValidator<SenhaForte, String> {

    @Getter
    @AllArgsConstructor
    private static class ItemValidacao {
        String regex;
        String mensagem;
    }

    private static final ItemValidacao[] ITENS = {
            new ItemValidacao(".{8,}", "{senha.forte.regras.tamanho8}"),
            new ItemValidacao(".*[A-Z].*", "{senha.forte.regras.maiuscula}"),
            new ItemValidacao(".*[a-z].*", "{senha.forte.regras.minuscula}"),
            new ItemValidacao(".*[0-9].*", "{senha.forte.regras.digito}"),
            new ItemValidacao(".*[^A-Za-z0-9].*", "{senha.forte.regras.char-especial}"),
            new ItemValidacao("[^\\s]*", "{senha.forte.regras.nao.branco}"),
    };

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // não valida neste caso
        }
        String textoItens = Arrays.stream(ITENS)
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
