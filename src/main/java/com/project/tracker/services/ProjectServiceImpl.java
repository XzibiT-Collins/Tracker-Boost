package com.project.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tracker.dto.requestDto.ProjectRequestDto;
import com.project.tracker.dto.responseDto.ProjectResponseDto;
import com.project.tracker.exceptions.customExceptions.ProjectNotFoundException;
import com.project.tracker.models.AuditLog;
import com.project.tracker.models.Project;
import com.project.tracker.repositories.ProjectRepository;
import com.project.tracker.services.serviceInterfaces.AuditLogService;
import com.project.tracker.services.serviceInterfaces.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Date;
import java.time.LocalDate;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ObjectMapper objectMapper;
    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              ObjectMapper objectMapper,
                              AuditLogService auditLogService) {
        this.projectRepository = projectRepository;
        this.objectMapper = objectMapper;
        this.auditLogService = auditLogService;
    }

    @Override
    public ProjectResponseDto addProject(ProjectRequestDto requestDto) {
        Project project = Project.builder()
                .projectName(requestDto.projectName())
                .description(requestDto.description())
                .deadline(requestDto.deadline())
                .status(requestDto.status())
                .build();

        Project savedProject = projectRepository.save(project);
        logAudit("Create Project", String.valueOf(savedProject.getId()), savedProject.getProjectName(), savedProject);

        return objectMapper.convertValue(savedProject, ProjectResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteProject(int id) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID: " + id + " not found"));

        projectRepository.deleteById(id);
        logAudit("Delete Project", String.valueOf(project.getId()), project.getProjectName(), project);
    }

    @Override
    public ProjectResponseDto updateProject(int id, ProjectRequestDto requestDto) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException("Project with ID: " + id + " not found");
        }

        Project updatedProject = Project.builder()
                .id(id)
                .projectName(requestDto.projectName())
                .description(requestDto.description())
                .deadline(requestDto.deadline())
                .status(requestDto.status())
                .build();

        Project savedProject = projectRepository.save(updatedProject);
        logAudit("Update Project", String.valueOf(savedProject.getId()), savedProject.getProjectName(), savedProject);

        return objectMapper.convertValue(savedProject, ProjectResponseDto.class);
    }

    @Override
    public ProjectResponseDto getProjectById(int id) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID: " + id + " not found"));

        logAudit("Get Project By ID", String.valueOf(project.getId()), project.getProjectName(), project);

        return objectMapper.convertValue(project, ProjectResponseDto.class);
    }

    @Override
    public Page<ProjectResponseDto> getAllProjects(int pageNumber, String sortBy) {
        System.out.println("Fetching All Task from db");
        int paginateBy = 10;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        Page<Project> projects = projectRepository.findAll(pageable);

        // Optional: log general access (not per item) if needed
        logAudit("Get All Projects", "PAGE_" + pageNumber, "None", "Project");

        return projects.map(project -> objectMapper.convertValue(project, ProjectResponseDto.class));
    }

    @Override
    public Page<ProjectResponseDto> getAllProjectsByTasksIsEmpty(int pageNumber, String sortBy) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        Page<Project> projects = projectRepository.findAllByTasksIsEmpty(pageable);

        logAudit("Get Projects Without Tasks","PAGE_"+pageNumber,"None","Project");
        return projects
                .map(project -> objectMapper.convertValue(project, ProjectResponseDto.class));
    }

    private void logAudit(String actionType, String entityId, String actorName, Object entity) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            payload = "Could not serialize payload: " + e.getMessage();
        }

        auditLogService.addAuditLog(AuditLog.builder()
                .actionType(actionType)
                .entityId(entityId)
                .actorName(actorName)
                .payload(payload)
                .timestamp(Date.valueOf(LocalDate.now()))
                .build());
    }
}
