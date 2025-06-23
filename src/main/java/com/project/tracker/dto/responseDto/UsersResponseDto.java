package com.project.tracker.dto.responseDto;

import com.project.tracker.models.Task;
import com.project.tracker.models.authmodels.Role;

import java.util.List;
import java.util.Set;

public record UsersResponseDto(
    int id,
    String name,
    String email,
    Set<String> skills,
    List<Task>tasks,
    Role role
) {}
