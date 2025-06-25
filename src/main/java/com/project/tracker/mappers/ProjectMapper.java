package com.project.tracker.mappers;

import com.project.tracker.dto.requestDto.ProjectRequestDto;
import com.project.tracker.dto.responseDto.ProjectResponseDto;
import com.project.tracker.models.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public static Project mapToProject(ProjectRequestDto requestDto){
        return Project.builder()
                .projectName(requestDto.projectName())
                .description(requestDto.description())
                .deadline(requestDto.deadline())
                .status(requestDto.status())
                .build();
    }

    public static ProjectResponseDto mapToProjectResponseDto(Project project){
        return ProjectResponseDto.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .deadline(project.getDeadline())
                .status(project.getStatus())
                .tasks(project.getTasks())
                .build();
    }
}
