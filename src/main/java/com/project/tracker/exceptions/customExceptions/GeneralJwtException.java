package com.project.tracker.exceptions.customExceptions;

public class GeneralJwtException extends RuntimeException {
    public GeneralJwtException(String message) {
        super(message);
    }
}
