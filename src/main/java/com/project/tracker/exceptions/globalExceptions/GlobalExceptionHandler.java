package com.project.tracker.exceptions.globalExceptions;

import com.project.tracker.exceptions.customExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler{
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status){

        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .message(message)
                        .timestamp(new Date())
                        .status(status.value())
                        .build()
        );
    }

    @ExceptionHandler(GeneralJwtException.class)
    public ResponseEntity<ErrorResponse> handleGeneralJwtException(GeneralJwtException exception){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return buildErrorResponse(exception.getMessage(), status);
    }

    @ExceptionHandler(ExpiredJwtTokenException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtTokenException(ExpiredJwtTokenException exception){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return buildErrorResponse(exception.getMessage(), status);
    }

    @ExceptionHandler(InvalidLoginDetailsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoginDetailsException(InvalidLoginDetailsException exception){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildErrorResponse(exception.getMessage(), status);
    }

    @ExceptionHandler(UserRoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(UserRoleNotFoundException exception){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return buildErrorResponse(exception.getMessage(), status);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExistException(UserAlreadyExistException exception){
        HttpStatus status = HttpStatus.CONFLICT;
        return buildErrorResponse(exception.getMessage(), status);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProjectNotFound(ProjectNotFoundException exception) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return buildErrorResponse(exception.getMessage(), notFound);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException exception){
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return buildErrorResponse(exception.getMessage(), notFound);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeveloperNotFound(UserNotFoundException exception){
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return buildErrorResponse(exception.getMessage(), notFound);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        String errorMessage = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField()+ " : "+ error.getDefaultMessage())
                .collect(Collectors.joining("\n"));

        return buildErrorResponse(errorMessage, badRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception exception){
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        return buildErrorResponse(exception.getMessage(), internalServerError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exception){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return buildErrorResponse(exception.getMessage(), badRequest);
    }
}
