package com.nickmafra.demo.infra.security;

import com.nickmafra.demo.model.Usuario;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class JwtUserDetails {

    private final long idUsuario;
    private final Collection<Papel> papeis; // futuramente um usuário poderá possuir mais de um papel

    /** Contém os papéis e também as operações de cada papel */
    private final Collection<GrantedAuthority> authorities;

    public JwtUserDetails(long idUsuario, Collection<Papel> papeis) {
        this.idUsuario = idUsuario;
        this.papeis = Collections.unmodifiableCollection(papeis);
        this.authorities = criarAuthorities();
    }

    public JwtUserDetails(Usuario usuario) {
        this.idUsuario = usuario.getId();
        this.papeis = Collections.singletonList(usuario.getPapel());
        this.authorities = criarAuthorities();
    }

    public JwtUserDetails(long idUsuario, Papel... papeis) {
        this.idUsuario = idUsuario;
        this.papeis = Collections.unmodifiableCollection(Arrays.asList(papeis));
        this.authorities = criarAuthorities();
    }

    private Collection<GrantedAuthority> criarAuthorities() {
        return Stream.concat(
                papeis.stream(),
                papeis.stream().map(Papel::getOperacoes).flatMap(Collection::stream))
                .collect(Collectors.toList());
    }
}
