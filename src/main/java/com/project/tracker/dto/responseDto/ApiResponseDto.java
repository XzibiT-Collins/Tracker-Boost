package com.project.tracker.dto.responseDto;

import com.project.tracker.exceptions.globalExceptions.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {
    private T data;
    private String message;
    private boolean success = false;
    private int status;
    private Date timestamp;
    private List<String> errors;


    public static <T> ApiResponseDto<T> success(T data, int status){
        return ApiResponseDto.<T>builder()
                .data(data)
                .success(true)
                .status(status)
                .timestamp(new Date())
                .build();
    }

    public static <T> ApiResponseDto<T> error(ErrorResponse data, List<String> errors){
        return ApiResponseDto.<T>builder()
                .data(null)
                .message(data.getMessage())
                .status(data.getStatus())
                .timestamp(data.getTimestamp())
                .errors(errors)
                .build();
    }
}
