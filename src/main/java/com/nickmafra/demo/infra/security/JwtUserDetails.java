package com.nickmafra.demo.infra.security;

import com.nickmafra.demo.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserDetails {

    private long idUsuario;
    private Collection<AppAuthority> authorities;

    public JwtUserDetails(Usuario usuario) {
        this.idUsuario = usuario.getId();
        this.authorities = Arrays.asList(AppAuthority.values()); // TODO alterar após o usuário ter os papéis
    }
}
