package com.project.tracker.controllers;

import com.project.tracker.dto.requestDto.ProjectRequestDto;
import com.project.tracker.dto.responseDto.ApiResponseDto;
import com.project.tracker.dto.responseDto.ProjectResponseDto;
import com.project.tracker.services.serviceInterfaces.ProjectService;
import com.project.tracker.sortingEnums.ProjectSorting;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping("")
    @CacheEvict(value = "projects", allEntries = true)
    public ResponseEntity<ApiResponseDto<ProjectResponseDto>> createProject(@Valid @RequestBody ProjectRequestDto request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(projectService.addProject(request),HttpStatus.CREATED.value()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @CacheEvict(value = "projects", allEntries = true)
    public ResponseEntity<ApiResponseDto<String>> deleteProject(@PathVariable int id){
        projectService.deleteProject(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success("project deleted successfully",HttpStatus.OK.value()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    @CacheEvict(value = "projects", allEntries = true)
    public ResponseEntity<ApiResponseDto<ProjectResponseDto>> updateProject(@PathVariable int id, @Valid @RequestBody ProjectRequestDto request ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(projectService.updateProject(id,request),HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "projects", key = "#id")
    public ResponseEntity<ApiResponseDto<ProjectResponseDto>> getProject(@PathVariable int id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(projectService.getProjectById(id),HttpStatus.OK.value()));
    }

    @GetMapping
    @Cacheable(value = "projects", key = "T(java.util.Objects).hash(#pageNumber, #sortBy)")
    public ResponseEntity<ApiResponseDto<Page<ProjectResponseDto>>> getAllProjects(
            @RequestParam(required = false, defaultValue = "SORT_BY_ID") ProjectSorting sortBy,
            @RequestParam(required = false, defaultValue = "0") int pageNumber
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(projectService.getAllProjects(pageNumber,sortBy.getField()),HttpStatus.OK.value()));
    }

    @Cacheable(value = "projectsWithoutTasks")
    @GetMapping("/withoutTasks")
    public ResponseEntity<ApiResponseDto<Page<ProjectResponseDto>>> getProjectsWithoutTasks(
            @RequestParam(required = false, defaultValue = "SORT_BY_ID") ProjectSorting sortBy,
            @RequestParam(required = false, defaultValue = "0") int pageNumber
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDto.success(projectService.getAllProjectsByTasksIsEmpty(pageNumber,sortBy.getField()),HttpStatus.OK.value()));
    }
}
