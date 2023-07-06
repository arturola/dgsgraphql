package com.arturola.graphql.service.command;

import com.arturola.graphql.datasource.entity.Problemz;
import com.arturola.graphql.datasource.entity.Solutionz;
import com.arturola.graphql.datasource.repository.SolutionzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Optional;

@Service
public class SolutionzCommandService {
    private Sinks.Many<Solutionz> solutionzSink = Sinks.many().multicast().onBackpressureBuffer();
    @Autowired
    private SolutionzRepository solutionzRepository;
    public Solutionz createSolutionz(Solutionz solutionz) {
        return solutionzRepository.save(solutionz);
    }

    public Optional<Solutionz> addBadVote(String id) {
        solutionzRepository.addBadVotesCount(id);

        var updatedSolution = solutionzRepository.findById(id);

        if(updatedSolution.isPresent()) {
            solutionzSink.tryEmitNext(updatedSolution.get());
        }

        return updatedSolution;
    }

    public Optional<Solutionz> addGoodVote(String id) {
        solutionzRepository.addGoodVotesCount(id);
        var updatedSolution = solutionzRepository.findById(id);

        if(updatedSolution.isPresent()) {
            solutionzSink.tryEmitNext(updatedSolution.get());
        }

        return updatedSolution;
    }

    public Flux<Solutionz> solutionzFlux() {
        return solutionzSink.asFlux();
    }
}
