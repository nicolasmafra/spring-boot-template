package com.nickmafra.demo.infra.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Representa uma operação no sistema em formato enum e no mesmo padrão do Spring Security.
 * Cada {@link Papel} possui uma lista dessas operações.
 */
@Getter
@RequiredArgsConstructor
public enum Operacao implements GrantedAuthority {

    CRIACAO(Acesso.OP_CRIACAO),
    LEITURA(Acesso.OP_LEITURA),
    ALTERACAO(Acesso.OP_ALTERACAO),
    DELECAO(Acesso.OP_DELECAO),
    ;

    private final String authority;
}
