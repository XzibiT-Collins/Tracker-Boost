package com.project.tracker.dto.requestDto;

import com.project.tracker.sortingEnums.UserRolesEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public record UsersRequestDto(
    @NotBlank(message = "Name cannot be blank.")
    String name,

    @NotBlank(message = "Please provide an email.")
    @Email(message = "Please provide a valid email.")
    String email,

    @NotBlank(message = "Please provide a password.")
    String password,

    @NotEmpty(message = "Please add a skill.")
    Set<String> skills,

    @NotNull(message = "Assign a role to the user")
    @Enumerated(EnumType.STRING)
    UserRolesEnum role

){}
