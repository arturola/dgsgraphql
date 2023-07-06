package com.arturola.graphql.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class HttpConfigurer extends AbstractHttpConfigurer<HttpConfigurer, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        var authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(
                new SecurityFilter(authenticationManager),
                UsernamePasswordAuthenticationFilter.class
        );
    }

    public static HttpConfigurer newInstance() {
        return new HttpConfigurer();
    }

}
