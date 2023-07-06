package com.arturola.graphql.exception;

public class ProblemzAuthenticationException extends RuntimeException {
    public ProblemzAuthenticationException() {
        super("Credenciales no validas");
    }
}
