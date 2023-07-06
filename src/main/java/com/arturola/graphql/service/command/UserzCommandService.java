package com.arturola.graphql.service.command;

import com.arturola.graphql.datasource.entity.Userz;
import com.arturola.graphql.datasource.entity.UserzToken;
import com.arturola.graphql.datasource.repository.UserzRepository;
import com.arturola.graphql.datasource.repository.UserzTokenRepository;
import com.arturola.graphql.exception.ProblemzAuthenticationException;
import com.arturola.graphql.util.HashUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserzCommandService {
    @Autowired
    private UserzRepository userzRepository;

    @Autowired
    private UserzTokenRepository userzTokenRepository;

    public UserzToken login(String username, String password) {
        var userzQuery = userzRepository.findByUsernameIgnoreCase(username);

        if(userzQuery.isEmpty() || !HashUtil.isBycriptMatch(password, userzQuery.get().getHashedPassword())) {
            // throw new IllegalArgumentException("El usuario no existe");
            throw new ProblemzAuthenticationException();
        }

        var randomAuthToken = RandomStringUtils.randomAlphanumeric(40);

        return refreshToken(userzQuery.get().getId(), randomAuthToken);
    }

    @NotNull
    private UserzToken refreshToken(String userId, String token) {
        var userzToken = new UserzToken();
        var now = LocalDateTime.now();

        userzToken.setUserId(userId);
        userzToken.setAuthToken(token);
        userzToken.setCreationTimestamp(now);
        userzToken.setExpiryTimestamp(now.plusHours(2));

        return userzTokenRepository.save(userzToken);
    }

    public Userz createUserz(Userz userz) {
        return  userzRepository.save(userz);
    }

    public Optional<Userz> activateUser(String username, Boolean isActive) {
        userzRepository.activateUserz(username, isActive);

        return userzRepository.findByUsernameIgnoreCase(username);
    }
}

