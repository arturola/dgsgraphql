package com.arturola.graphql.exception;

public class ProblemzPermitionException extends RuntimeException {
    public ProblemzPermitionException() {
        super("Ud. no tiene permiso para acceder a esta operacion / recurso.");
    }
}
