package com.nickmafra.demo.infra.security;

/**
 * Cada permissão é uma String constante equivalente a um {@link Papel} ou uma {@link Operacao}.
 *
 * Necessário fazer essa duplicação para poder referenciar via anocação.
 */
public class Acesso {

    public static final String PAPEL_ADMIN = "ROLE_ADMIN";
    public static final String PAPEL_COMUM = "ROLE_COMUM";

    public static final String OP_CRIACAO = "OP_CREATE";
    public static final String OP_LEITURA = "OP_READ";
    public static final String OP_ALTERACAO = "OP_UPDATE";
    public static final String OP_DELECAO = "OP_DELETE";

    public static final String ADMIN_OU_PROPRIO_USUARIO = "hasRole('" + PAPEL_ADMIN + "') or "
            + "#id == authentication.id";

    private Acesso() {}
}
