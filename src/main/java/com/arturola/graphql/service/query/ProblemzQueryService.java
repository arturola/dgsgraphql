package com.arturola.graphql.service.query;

import com.arturola.graphql.datasource.entity.Problemz;
import com.arturola.graphql.datasource.repository.ProblemzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemzQueryService {
    @Autowired
    private ProblemzRepository problemzRepository;

    public List<Problemz> problemLatestList() {
       return problemzRepository.findAllByOrderByCreationTimestampDesc();
    }

    public Optional<Problemz> problemDetail(String id) {
        return problemzRepository.findById(id);
    }

    public List<Problemz> problemzByKeyword(String keyword) {
        return problemzRepository.findByKeyword("%" + keyword + "%");
    }
}
