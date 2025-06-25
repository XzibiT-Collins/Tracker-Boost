package com.project.tracker.dto.responseDto;

import com.project.tracker.models.Task;
import com.project.tracker.models.authmodels.Role;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record UsersResponseDto(
    int id,
    String name,
    String email,
    Set<String> skills,
    List<Task>tasks,
    String role
) {}
