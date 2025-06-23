package com.project.tracker.dto.requestDto;

import com.project.tracker.sortingEnums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.sql.Date;

@Builder
public record ProjectRequestDto(
    @NotBlank(message = "Project name must not be blank.")
    String projectName,

    @Size(max = 500, message = "Description cannot be more than 500 characters")
    @NotBlank(message = "Project must have a description.")
    String description,

    @NotNull(message = "Project must have a deadline.")
    Date deadline,

    @NotNull(message = "Project must have a status.")
    StatusEnum status
) {}
