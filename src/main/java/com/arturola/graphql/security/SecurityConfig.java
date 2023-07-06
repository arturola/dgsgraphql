package com.arturola.graphql.security;

import com.arturola.graphql.datasource.repository.UserzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserzRepository userzRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var authProvider = new ProblemzAuthenticationProvider(userzRepository);

        http.apply(HttpConfigurer.newInstance());
        http.authenticationProvider(authProvider);

        return http.build();
    }

}



