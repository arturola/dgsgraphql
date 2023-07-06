package com.arturola.graphql.exception.handle;

import com.netflix.graphql.types.errors.ErrorDetail;
import com.netflix.graphql.types.errors.ErrorType;

public class ProblemzErrorDetail implements ErrorDetail {
    @Override
    public ErrorType getErrorType() {
        return ErrorType.UNAUTHENTICATED;
    }

    @Override
    public String toString() {
        return "El usuario no pudo ser validado.";
    }
}
