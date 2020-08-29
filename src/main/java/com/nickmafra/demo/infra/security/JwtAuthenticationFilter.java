package com.nickmafra.demo.infra.security;

import com.nickmafra.demo.infra.exception.TokenExpiradoException;
import com.nickmafra.demo.infra.exception.TokenInvalidoException;
import com.nickmafra.demo.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filtro que verifica se possui token de autenticação e, se houver, adiciona um objeto AuthenticationToken no contexto.
 */
@Slf4j
public class JwtAuthenticationFilter implements Filter, AuthenticationConverter, AuthenticationManager,
        AuthenticationProvider, AuthenticationSuccessHandler, AuthenticationFailureHandler {

    public static final String AUTH_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final AuthenticationFilter authenticationFilter;

    public JwtAuthenticationFilter(JwtService jwtService, String pattern) {
        this.jwtService = jwtService;
        this.authenticationFilter = new AuthenticationFilter(this, this);

        authenticationFilter.setRequestMatcher(new AntPathRequestMatcher(pattern));
        authenticationFilter.setSuccessHandler(this);
        authenticationFilter.setFailureHandler(this);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        authenticationFilter.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        authenticationFilter.doFilter(request, response, chain);
    }

    @Override
    public void destroy() {
        authenticationFilter.destroy();
    }

    @Override
    public JwtAuthentication convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null)
            return null;

        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTH_PREFIX))
            return null;

        try {
            String strJwt = header.substring(AUTH_PREFIX.length());
            return jwtService.parse(strJwt);
        } catch (IllegalArgumentException | TokenInvalidoException e) {
            throw new BadCredentialsException("Failed to decode JWT authentication bearer token");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationClazz) {
        return JwtAuthentication.class.equals(authenticationClazz);
    }

    @Override
    public JwtAuthentication authenticate(Authentication authentication) {
        if (!supports(authentication.getClass()))
            return null;

        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;

        if (jwtAuthentication.isAuthenticated())
            return null;

        return jwtService.autenticar(jwtAuthentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
        // o comportamento padrão é seguir em frente
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException {
        String mensagem = e.getCause() instanceof TokenExpiradoException ? "Token expirado!" : "Erro na autenticação.";
        log.debug(mensagem, e);

        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());
        res.setContentType("text/plain");
        res.getWriter().print(mensagem);
    }
}
