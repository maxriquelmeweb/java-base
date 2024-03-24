package com.riquelme.springbootcrudhibernaterestful.security.filter;

import static com.riquelme.springbootcrudhibernaterestful.security.TokenJwtConfig.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.login.CustomDeserializationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final MessageSource messageSource;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, MessageSource messageSource) {
        this.authenticationManager = authenticationManager;
        this.messageSource = messageSource;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        User user = null;
        String email = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (Exception e) {
            throw new CustomDeserializationException("user.deserialization.error", e);
        }

        email = user.getEmail();
        password = user.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult
                .getPrincipal();
        String email = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("email", email)
                .build();

        String token = Jwts.builder()
                .subject(email)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("email", email);
        body.put("message", getMessageSource("auth.success"));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("message", getMessageSource("auth.error"));
        body.put("error", getMessageSource("auth.badCredentials"));
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);
    }

    private String getMessageSource(String messageKey){
        Locale locale = LocaleContextHolder.getLocale();
        return  messageSource.getMessage(messageKey, null, locale);
    }
}
