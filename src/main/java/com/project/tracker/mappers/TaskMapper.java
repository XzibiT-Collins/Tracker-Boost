package com.project.tracker.mappers;

import com.project.tracker.dto.requestDto.TaskRequestDto;
import com.project.tracker.dto.responseDto.TaskResponseDto;
import com.project.tracker.models.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public static Task mapToTask(TaskRequestDto requestDto){
        return Task.builder()
                .title(requestDto.title())
                .description(requestDto.description())
                .dueDate(requestDto.dueDate())
                .status(requestDto.status())
                .build();
    }

    public static TaskResponseDto mapToTaskResponseDto(Task task){
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate().toString())
                .users(task.getUsers().getName())
                .project(task.getProject().getProjectName())
                .build();
    }
}
