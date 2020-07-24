package com.nickmafra.demo.infra.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro que verifica se possui token de autenticação e, se houver, adiciona um objeto AuthenticationToken no contexto.
 */
@Slf4j
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String strToken = req.getHeader("Authorization");

        if (StringUtils.isEmpty(strToken)) {
            chain.doFilter(req, res);
            return; // prossegue
        }
        log.info("Autenticação via token encontrada.");

        AuthenticationToken token;
        try {
            token = parseToken(strToken);
        } catch (Exception e) {
            throw new BadCredentialsException("Formato de token inválido.");
        }

        SecurityContextHolder.getContext().setAuthentication(token); // insere Authentication no contexto
        chain.doFilter(req, res); // prossegue
    }

    private AuthenticationToken parseToken(String strToken) {
        // TODO quando for JWT terá um processo de conversão aqui
        return new AuthenticationToken(strToken);
    }
}
