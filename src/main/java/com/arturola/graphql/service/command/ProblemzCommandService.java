package com.arturola.graphql.service.command;

import com.arturola.graphql.datasource.entity.Problemz;
import com.arturola.graphql.datasource.repository.ProblemzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class ProblemzCommandService {
    private Sinks.Many<Problemz> problemzSink = Sinks.many().multicast().onBackpressureBuffer();

    @Autowired
    private ProblemzRepository problemzRepository;

    public Problemz createProblemz(Problemz problemz) {
        var newProblemz = problemzRepository.save(problemz);

        // Emite el nuevo problema
        problemzSink.tryEmitNext(newProblemz);

        return newProblemz;
    }
    public Flux<Problemz> problemzFlux() {
        return problemzSink.asFlux();
    }
}
