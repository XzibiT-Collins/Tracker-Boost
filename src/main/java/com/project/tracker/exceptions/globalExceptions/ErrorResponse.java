package com.project.tracker.exceptions.globalExceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Builder
public class ErrorResponse {
    public String message;
    public Date timestamp;
    public int status;
}
