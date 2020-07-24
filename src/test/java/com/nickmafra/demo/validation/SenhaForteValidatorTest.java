package com.nickmafra.demo.validation;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class SenhaForteValidatorTest {

    @Test
    void testTamanho8() {
        Pattern pattern = Pattern.compile(SenhaForteValidator.ItemValidacao.TAMANHO8.regex);
        assertThat(pattern.matcher("1234567").matches()).isFalse();
        assertThat(pattern.matcher("12345678").matches()).isTrue();
        assertThat(pattern.matcher("123456789").matches()).isTrue();
    }

    @Test
    void testMaiuscula() {
        Pattern pattern = Pattern.compile(SenhaForteValidator.ItemValidacao.MAIUSCULA.regex);
        assertThat(pattern.matcher("abc8@fgh").matches()).isFalse();
        assertThat(pattern.matcher("abc8@fgH").matches()).isTrue();
        assertThat(pattern.matcher("Abc8@fgH").matches()).isTrue();
    }

    @Test
    void testMinuscula() {
        Pattern pattern = Pattern.compile(SenhaForteValidator.ItemValidacao.MINUSCULA.regex);
        assertThat(pattern.matcher("ABC8@FGH").matches()).isFalse();
        assertThat(pattern.matcher("ABC8@FGh").matches()).isTrue();
        assertThat(pattern.matcher("aBC8@FGh").matches()).isTrue();
    }

    @Test
    void testDigito() {
        Pattern pattern = Pattern.compile(SenhaForteValidator.ItemValidacao.DIGITO.regex);
        assertThat(pattern.matcher("AbCD@FGH").matches()).isFalse();
        assertThat(pattern.matcher("AbCD@FG1").matches()).isTrue();
        assertThat(pattern.matcher("0bCD@FG1").matches()).isTrue();
    }

    @Test
    void testCharEspecial() {
        Pattern pattern = Pattern.compile(SenhaForteValidator.ItemValidacao.CHAR_ESPECIAL.regex);
        assertThat(pattern.matcher("AbcDe8gh").matches()).isFalse();
        assertThat(pattern.matcher("AbcDe8g@").matches()).isTrue();
        assertThat(pattern.matcher("!bcDe8g@").matches()).isTrue();
    }

    @Test
    void testNaoBrancos() {
        Pattern pattern = Pattern.compile(SenhaForteValidator.ItemValidacao.NAO_BRANCOS.regex);
        assertThat(pattern.matcher("AbcDE8gh").matches()).isTrue();
        assertThat(pattern.matcher("AbcD E8gh").matches()).isFalse();
        assertThat(pattern.matcher("AbcD  E8gh").matches()).isFalse();
        assertThat(pattern.matcher("AbcD\tE8gh").matches()).isFalse();
        assertThat(pattern.matcher("AbcD\t\tE8gh").matches()).isFalse();
        assertThat(pattern.matcher("  AbcD\t\tE8gh\t\t").matches()).isFalse();
    }

}