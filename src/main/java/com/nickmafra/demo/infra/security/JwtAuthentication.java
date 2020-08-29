package com.nickmafra.demo.infra.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class JwtAuthentication extends PreAuthenticatedAuthenticationToken {

    public JwtAuthentication(DecodedJWT decodedJWT) {
        super(decodedJWT.getSubject(), decodedJWT);
    }

    public JwtAuthentication(DecodedJWT decodedJWT, JwtUserDetails jwtUserDetails) {
        super(decodedJWT.getSubject(), decodedJWT, jwtUserDetails.getAuthorities());
        setDetails(jwtUserDetails);
    }

    @Override
    public String getPrincipal() {
        return (String) super.getPrincipal();
    }

    @Override
    public DecodedJWT getCredentials() {
        return (DecodedJWT) super.getCredentials();
    }

    @Override
    public JwtUserDetails getDetails() {
        return (JwtUserDetails) super.getDetails();
    }

    @Override
    public void setDetails(Object details) {
        if (details != null && !(details instanceof JwtUserDetails))
            throw new IllegalArgumentException("Must be a JwtDetails.");

        super.setDetails(details);
    }
}
