package com.nickmafra.demo.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum AppAuthority implements GrantedAuthority {
    MASTER("ROLE_MASTER"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    public static AppAuthority buscarPorAuthority(String authority) {
        return Arrays.stream(values())
                .filter(appAuthority -> appAuthority.authority.equalsIgnoreCase(authority))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static String[] toStringArray(Collection<AppAuthority> authorities) {
        return authorities.stream().map(AppAuthority::getAuthority).toArray(String[]::new);
    }

    public static Collection<AppAuthority> fromStringArray(String[] authorities) {
        return Arrays.stream(authorities).map(AppAuthority::buscarPorAuthority).collect(Collectors.toList());
    }
}
