package com.arturola.graphql.service.query;

import com.arturola.graphql.datasource.entity.Solutionz;
import com.arturola.graphql.datasource.repository.SolutionzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolutionzQueryService {
    @Autowired
    private SolutionzRepository solutionzRepository;
    public List<Solutionz> problemzByKeyword(String keyword) {
        return solutionzRepository.findSolutionsByKeyword("%" + keyword + "%");
    }

    public Optional<Solutionz> getSolutionz(String id) {
        return solutionzRepository.findById(id);
    }
}
