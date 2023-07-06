package com.arturola.graphql.component.problemz;

import com.arturola.graphql.exception.ProblemzAuthenticationException;
import com.arturola.graphql.exception.handle.ProblemzErrorDetail;
import com.arturola.graphql.service.command.ProblemzCommandService;
import com.arturola.graphql.service.query.ProblemzQueryService;
import com.arturola.graphql.service.query.UserzQueryService;
import com.arturola.graphql.util.GraphBeanMapper;
import com.netflix.dgs.codegen.generated.DgsConstants;
import com.netflix.dgs.codegen.generated.types.Problem;
import com.netflix.dgs.codegen.generated.types.ProblemCreateInput;
import com.netflix.dgs.codegen.generated.types.ProblemResponse;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class ProblemDataResolver {
    @Autowired
    private ProblemzQueryService problemzQueryService;
    @Autowired
    private ProblemzCommandService problemzCommandService;
    @Autowired
    private UserzQueryService userzQueryService;

    @DgsQuery(field = DgsConstants.QUERY.ProblemLatestList)
    public List<Problem> getProblems() {
        return problemzQueryService.problemLatestList().stream()
                .map(GraphBeanMapper::mapToGraphql).collect(Collectors.toList());
    }

    @DgsQuery(field = DgsConstants.QUERY.ProblemDetail)
    public Problem getProblem(@InputArgument(name = "id") String problemId) {
        //var problemz = problemzQueryService.problemDetail(problemId).get();
        var problemz = problemzQueryService.problemDetail(problemId).orElseThrow(DgsEntityNotFoundException::new);

        return GraphBeanMapper.mapToGraphql(problemz);
    }

    @DgsMutation(field = DgsConstants.MUTATION.ProblemCreate)
    public ProblemResponse createProblem(
            @RequestHeader(name = "authToken" ,required = true) String authToken,
            @InputArgument(name="problem")ProblemCreateInput problemCreateInput) {

        var userz = userzQueryService.findUserByAuthToken(authToken).orElseThrow(ProblemzAuthenticationException::new);

        var problemz = GraphBeanMapper.mapToEntity(problemCreateInput, userz);

        var newProblemz = problemzCommandService.createProblemz(problemz);

        var problem = GraphBeanMapper.mapToGraphql(newProblemz);

        return ProblemResponse.newBuilder()
                .problem(problem)
                .build();
    }


    @DgsSubscription(field = DgsConstants.SUBSCRIPTION.ProblemAdded)
    public Flux<Problem> subscribeProblemAdded() {

        return problemzCommandService.problemzFlux().map(GraphBeanMapper::mapToGraphql);
    }
}
