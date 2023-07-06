package com.arturola.graphql.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    public SecurityFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = request.getHeader("authToken");

        if(StringUtils.isBlank(token)) {
            token = StringUtils.EMPTY;
        }

       // if(token != null) {
            var authentication = new UsernamePasswordAuthenticationToken(null, token);

            var auth = authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(auth);
        // }

        filterChain.doFilter(request, response);
    }

}
