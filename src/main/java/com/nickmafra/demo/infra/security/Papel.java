package com.nickmafra.demo.infra.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Representa um papel de usuário em formato enum e no mesmo padrão do Spring Security.
 *
 * Cada papel possui uma lista de {@link Operacao} permitida.
 */
@Getter
public enum Papel implements GrantedAuthority {
    ADMIN(Acesso.PAPEL_ADMIN, Operacao.values()), // todas as operações
    COMUM(Acesso.PAPEL_COMUM, Operacao.CRIACAO, Operacao.LEITURA)
    ;

    private final String authority;
    private final Collection<Operacao> operacoes;

    Papel(String authority, Operacao... operations) {
        this.authority = authority;
        this.operacoes = Collections.unmodifiableCollection(Arrays.asList(operations));
    }

    public static Papel buscarPorAuthority(String authority) {
        return Arrays.stream(values())
                .filter(appAuthority -> appAuthority.authority.equalsIgnoreCase(authority))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static String[] toStringArray(Collection<Papel> authorities) {
        return authorities.stream().map(Papel::getAuthority).toArray(String[]::new);
    }

    public static Collection<Papel> fromStringArray(String[] authorities) {
        return Arrays.stream(authorities).map(Papel::buscarPorAuthority).collect(Collectors.toList());
    }
}
