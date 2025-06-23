package com.project.tracker.services.serviceInterfaces;

import com.project.tracker.dto.requestDto.ProjectRequestDto;
import com.project.tracker.dto.responseDto.ProjectResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto addProject(ProjectRequestDto requestDto);
    void deleteProject(int id);
    ProjectResponseDto updateProject(int id,ProjectRequestDto requestDto);
    ProjectResponseDto getProjectById(int id);
    Page<ProjectResponseDto> getAllProjects(int pageNumber, String sortBy);
    Page<ProjectResponseDto> getAllProjectsByTasksIsEmpty(int pageNumber, String sortBy);
}
