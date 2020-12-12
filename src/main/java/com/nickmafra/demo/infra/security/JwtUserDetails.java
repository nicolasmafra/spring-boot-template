package com.nickmafra.demo.infra.security;

import com.nickmafra.demo.model.Usuario;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class JwtUserDetails {

    private final long idUsuario;
    private final Set<Papel> papeis; // futuramente um usuário poderá possuir mais de um papel

    /** Contém os papéis e também as operações de cada papel */
    private final Set<GrantedAuthority> authorities;

    public JwtUserDetails(long idUsuario, Collection<Papel> papeis) {
        this.idUsuario = idUsuario;
        this.papeis = Collections.unmodifiableSet(new HashSet<>(papeis));
        this.authorities = criarAuthorities(papeis);
    }

    public JwtUserDetails(Usuario usuario) {
        this(usuario.getId(), usuario.getPapel());
    }

    public JwtUserDetails(long idUsuario, Papel... papeis) {
        this(idUsuario, Arrays.asList(papeis));
    }

    private static Set<GrantedAuthority> criarAuthorities(Collection<Papel> papeis) {
        return Stream.concat(
                papeis.stream(),
                papeis.stream().map(Papel::getOperacoes).flatMap(Collection::stream))
                .collect(Collectors.toSet());
    }
}
