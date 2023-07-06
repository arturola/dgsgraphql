package com.arturola.graphql.exception.handle;

import com.arturola.graphql.exception.ProblemzAuthenticationException;
import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler;
import com.netflix.graphql.types.errors.ErrorType;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;
import org.xml.sax.helpers.DefaultHandler;

import java.util.concurrent.CompletableFuture;

@Component
public class GraphqlExceptionHandler implements DataFetcherExceptionHandler {
    private final DefaultDataFetcherExceptionHandler defaultHandler = new DefaultDataFetcherExceptionHandler();

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        var exception = handlerParameters.getException();

        if(exception instanceof ProblemzAuthenticationException) {
            var graphqlError = TypedGraphQLError.newBuilder().message(exception.getMessage())
                    .path(handlerParameters.getPath())
                    //.errorType(ErrorType.UNAUTHENTICATED)
                    .errorDetail(new ProblemzErrorDetail())
                    .build();

            var result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            return CompletableFuture.completedFuture(result);
        }

        return defaultHandler.handleException(handlerParameters);
    }
}
