package com.project.tracker.exceptions.globalExceptions;

import com.project.tracker.dto.responseDto.ApiResponseDto;
import com.project.tracker.exceptions.customExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler{
    private ResponseEntity<ApiResponseDto<ErrorResponse>> buildErrorResponse(String message, HttpStatus status, List<String> errors){

        return ResponseEntity.status(status).body(ApiResponseDto.error(

                ErrorResponse.builder()
                        .message(message)
                        .timestamp(new Date())
                        .status(status.value())
                        .build(),errors
        ));
    }

    @ExceptionHandler(GeneralJwtException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleGeneralJwtException(GeneralJwtException exception){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return buildErrorResponse(exception.getMessage(), status,null);
    }

    @ExceptionHandler(ExpiredJwtTokenException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleExpiredJwtTokenException(ExpiredJwtTokenException exception){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return buildErrorResponse(exception.getMessage(), status,null);
    }

    @ExceptionHandler(InvalidLoginDetailsException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleInvalidLoginDetailsException(InvalidLoginDetailsException exception){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildErrorResponse(exception.getMessage(), status,null);
    }

    @ExceptionHandler(UserRoleNotFoundException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleRoleNotFoundException(UserRoleNotFoundException exception){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return buildErrorResponse(exception.getMessage(), status,null);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> userAlreadyExistException(UserAlreadyExistException exception){
        HttpStatus status = HttpStatus.CONFLICT;
        return buildErrorResponse(exception.getMessage(), status,null);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleProjectNotFound(ProjectNotFoundException exception) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return buildErrorResponse(exception.getMessage(), notFound,null);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleTaskNotFound(TaskNotFoundException exception){
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return buildErrorResponse(exception.getMessage(), notFound,null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleDeveloperNotFound(UserNotFoundException exception){
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return buildErrorResponse(exception.getMessage(), notFound,null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException exception){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        List<String> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField()+ " : "+ error.getDefaultMessage())
                .collect(Collectors.toList());

        return buildErrorResponse("Validation error occurred", badRequest,errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleGeneralExceptions(Exception exception){
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        return buildErrorResponse(exception.getMessage(), internalServerError,null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> handleBadRequest(IllegalArgumentException exception){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return buildErrorResponse(exception.getMessage(), badRequest,null);
    }
}
