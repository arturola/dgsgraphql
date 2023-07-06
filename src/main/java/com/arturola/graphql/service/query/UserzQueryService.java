package com.arturola.graphql.service.query;

import com.arturola.graphql.datasource.entity.Userz;
import com.arturola.graphql.datasource.repository.UserzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserzQueryService {
    @Autowired
    private UserzRepository userzRepository;

    public Optional<Userz> findUserByAuthToken(String authToken) {
        return userzRepository.findUserByToken(authToken);
    }
}
