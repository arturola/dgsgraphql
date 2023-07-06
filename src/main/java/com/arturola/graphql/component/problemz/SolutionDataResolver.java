package com.arturola.graphql.component.problemz;

import com.arturola.graphql.datasource.entity.Solutionz;
import com.arturola.graphql.exception.ProblemzAuthenticationException;
import com.arturola.graphql.service.command.SolutionzCommandService;
import com.arturola.graphql.service.query.ProblemzQueryService;
import com.arturola.graphql.service.query.SolutionzQueryService;
import com.arturola.graphql.service.query.UserzQueryService;
import com.arturola.graphql.util.GraphBeanMapper;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.Solution;
import com.netflix.dgs.codegen.generated.types.SolutionCreateInput;
import com.netflix.dgs.codegen.generated.types.SolutionResponse;
import com.netflix.dgs.codegen.generated.types.SolutionVoteInput;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import graphql.GraphQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Flux;

import java.util.List;

@DgsComponent
public class SolutionDataResolver {
    @Autowired
    private SolutionzQueryService solutionzQueryService;
    @Autowired
    private SolutionzCommandService solutionzCommandService;
    @Autowired
    private UserzQueryService userzQueryService;
    @Autowired
    private ProblemzQueryService problemzQueryService;

    @DgsMutation(field = DgsConstants.MUTATION.SolutionCreate)
    public SolutionResponse createSolution(
            @RequestHeader(name = "authToken", required = true) String authToken,
            @InputArgument(name = "solution")SolutionCreateInput solutionCreateInput) {

        var userz = userzQueryService.findUserByAuthToken(authToken).orElseThrow(ProblemzAuthenticationException::new);

        var problemz = problemzQueryService.problemDetail(solutionCreateInput.getProblemId()).orElseThrow(GraphQLException::new);

        var solutionz = GraphBeanMapper.mapToEntity(solutionCreateInput, userz, problemz);

        var solutionzGenerated = solutionzCommandService.createSolutionz(solutionz);

        var solution = GraphBeanMapper.mapToGraphql(solutionzGenerated);

        return SolutionResponse.newBuilder()
                .solution(solution)
                .build();
    }

    @DgsMutation(field = DgsConstants.MUTATION.SotutionVote)
    public SolutionResponse voteSolution(
            @RequestHeader(name = "authToken", required = true) String authToken,
            @InputArgument(name = "vote") SolutionVoteInput solutionVoteInput) {

        var solutionzId = solutionVoteInput.getSolutionId();

        //Check token, just to check
        userzQueryService.findUserByAuthToken(authToken).orElseThrow(ProblemzAuthenticationException::new);

        if(solutionVoteInput.getVoteAsGood())
            solutionzCommandService.addGoodVote(solutionzId);
        else
            solutionzCommandService.addBadVote(solutionzId);

        var solutionz = solutionzQueryService.getSolutionz(solutionzId).orElseThrow(DgsEntityNotFoundException::new);

        var solution = GraphBeanMapper.mapToGraphql(solutionz);

        return SolutionResponse.newBuilder()
                .solution(solution)
                .build();
    }

    @DgsSubscription(field = DgsConstants.SUBSCRIPTION.SolutionVoteChanged)
    public Flux<Solution> subscribeVoteAdded(@InputArgument(name = "solutionId") String solutionId) {
        return solutionzCommandService.solutionzFlux()
                .filter(solutionz -> solutionz.getId().equals(solutionId))
                .map(GraphBeanMapper::mapToGraphql);
    }
}
