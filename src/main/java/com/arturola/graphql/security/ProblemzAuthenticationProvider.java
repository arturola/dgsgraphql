package com.arturola.graphql.security;

import com.arturola.graphql.datasource.entity.Userz;
import com.arturola.graphql.datasource.repository.UserzRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ProblemzAuthenticationProvider implements AuthenticationProvider {
    private UserzRepository userzRepository;

    public ProblemzAuthenticationProvider(UserzRepository userzRepository) {
        this.userzRepository = userzRepository;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var userz = userzRepository.findUserByToken(authentication.getCredentials().toString()).orElse(new Userz());

        return new UsernamePasswordAuthenticationToken(userz, authentication.getCredentials().toString(), getAuthorities(userz.getUserRole()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
       return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        var authorities = new ArrayList<GrantedAuthority>();

        if(StringUtils.isNotBlank(role)) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
