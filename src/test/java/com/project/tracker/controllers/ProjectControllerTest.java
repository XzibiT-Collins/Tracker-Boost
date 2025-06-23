package com.project.tracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tracker.dto.requestDto.ProjectRequestDto;
import com.project.tracker.dto.responseDto.ProjectResponseDto;
import com.project.tracker.services.serviceInterfaces.ProjectService;
import com.project.tracker.sortingEnums.ProjectSorting;
import com.project.tracker.sortingEnums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@DisplayName("ProjectController Tests")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProjectRequestDto validRequestDto;
    private ProjectResponseDto validResponseDto;

    @BeforeEach
    void setUp() {
        validRequestDto = ProjectRequestDto.builder()
                .projectName("Test Project")
                .description("Test Description")
                .deadline(Date.valueOf(LocalDate.now().plusDays(30)))
                .status(StatusEnum.pending)
                .build();

        validResponseDto = ProjectResponseDto.builder()
                .id(1)
                .projectName("Test Project")
                .description("Test Description")
                .deadline(Date.valueOf(LocalDate.now().plusDays(30)))
                .status(StatusEnum.pending)
                .build();
    }

    @Nested
    @DisplayName("Create Project Tests")
    class CreateProjectTests {

        @Test
        @DisplayName("Should create project successfully with valid data")
        void shouldCreateProjectSuccessfully() throws Exception {
            when(projectService.addProject(any(ProjectRequestDto.class)))
                    .thenReturn(validResponseDto);

            mockMvc.perform(post("/api/v1/projects/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.projectName").value("Test Project"))
                    .andExpect(jsonPath("$.description").value("Test Description"));

            verify(projectService, times(1)).addProject(any(ProjectRequestDto.class));
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyInvalid() throws Exception {
            mockMvc.perform(post("/api/v1/projects/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"name\": \"invalid\" }"))
                    .andExpect(status().isBadRequest());

            verify(projectService, never()).addProject(any());
        }
    }

    @Nested
    @DisplayName("Get Project Tests")
    class GetProjectTests {

        @Test
        @DisplayName("Should get project by ID successfully")
        void shouldGetProjectByIdSuccessfully() throws Exception {
            when(projectService.getProjectById(1)).thenReturn(validResponseDto);

            mockMvc.perform(get("/api/v1/projects/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.projectName").value("Test Project"));

            verify(projectService, times(1)).getProjectById(1);
        }
    }

    @Nested
    @DisplayName("Update Project Tests")
    class UpdateProjectTests {

        @Test
        @DisplayName("Should update project successfully")
        void shouldUpdateProjectSuccessfully() throws Exception {
            when(projectService.updateProject(eq(1), any(ProjectRequestDto.class)))
                    .thenReturn(validResponseDto);

            mockMvc.perform(put("/api/v1/projects/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));

            verify(projectService, times(1)).updateProject(eq(1), any(ProjectRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Delete Project Tests")
    class DeleteProjectTests {

        @Test
        @DisplayName("Should delete project successfully")
        void shouldDeleteProjectSuccessfully() throws Exception {
            doNothing().when(projectService).deleteProject(1);

            mockMvc.perform(delete("/api/v1/projects/delete/1"))
                    .andExpect(status().isOk());

            verify(projectService, times(1)).deleteProject(1);
        }
    }

    @Nested
    @DisplayName("Get All Projects Tests")
    class GetAllProjectsTests {

        @Test
        @DisplayName("Should get all projects with default pagination")
        void shouldGetAllProjectsWithDefaultPagination() throws Exception {
            Page<ProjectResponseDto> mockPage = new PageImpl<>(Collections.singletonList(validResponseDto));
            when(projectService.getAllProjects(0, ProjectSorting.SORT_BY_ID.getField())).thenReturn(mockPage);

            mockMvc.perform(get("/api/v1/projects"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(1));

            verify(projectService, times(1)).getAllProjects(0, ProjectSorting.SORT_BY_ID.getField());
        }
    }
}