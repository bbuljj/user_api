package com.example.test.exception;

import org.springframework.validation.Errors;

public class ValidatorException extends RuntimeException {
    private final transient Errors errors;

    public ValidatorException(Errors errors) {
        super();
        this.errors = errors;
    }

    public Errors getError() {
        return errors;
    }
}