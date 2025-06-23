package com.project.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tracker.dto.requestDto.TaskRequestDto;
import com.project.tracker.dto.responseDto.StatusCountDto;
import com.project.tracker.dto.responseDto.TaskResponseDto;
import com.project.tracker.exceptions.customExceptions.UserNotFoundException;
import com.project.tracker.exceptions.customExceptions.ProjectNotFoundException;
import com.project.tracker.exceptions.customExceptions.TaskNotFoundException;
import com.project.tracker.models.AuditLog;
import com.project.tracker.models.Users;
import com.project.tracker.models.Project;
import com.project.tracker.models.Task;
import com.project.tracker.repositories.UsersRepository;
import com.project.tracker.repositories.ProjectRepository;
import com.project.tracker.repositories.TaskRepository;
import com.project.tracker.services.serviceInterfaces.AuditLogService;
import com.project.tracker.services.serviceInterfaces.StatusCountProjection;
import com.project.tracker.services.serviceInterfaces.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private Logger logger = Logger.getLogger(TaskServiceImpl.class.getName());
    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;
    private final UsersRepository usersRepository;
    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           ObjectMapper objectMapper,
                           UsersRepository usersRepository,
                           ProjectRepository projectRepository,
                           AuditLogService auditLogService) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
        this.usersRepository = usersRepository;
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public TaskResponseDto addTask(TaskRequestDto requestDto) {
        Users users = usersRepository
                .findById(requestDto.userId())
                .orElseThrow(() -> new UserNotFoundException(
                        "Users with ID: " + requestDto.userId() + " not found"));

        Project project = projectRepository
                .findById(requestDto.projectId())
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Project with ID: " + requestDto.projectId() + " not found"));

        Task task = Task.builder()
                .title(requestDto.title())
                .description(requestDto.description())
                .status(requestDto.status())
                .dueDate(requestDto.dueDate())
                .users(users)
                .project(project)
                .build();

        Task savedTask = taskRepository.save(task);
        logAudit("Create Task", String.valueOf(savedTask.getId()), savedTask.getTitle(), savedTask);

        return objectMapper.convertValue(savedTask, TaskResponseDto.class);
    }

    @Override
    public void deleteTask(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID: " + id + " not found"));

        taskRepository.deleteById(id);
        logAudit("Delete Task", String.valueOf(task.getId()), task.getTitle(), task);
    }

    @Override
    public TaskResponseDto updateTask(int id, TaskRequestDto requestDto) {
        logger.info("Task Request DTO: " + requestDto.toString() + "\n");
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID: " + id + " not found"));

        logger.info("User ID: " + requestDto.userId() + "\n");
        Users users = usersRepository.findById(requestDto.userId())
                .orElseThrow(() -> new UserNotFoundException("Users with ID: " + requestDto.userId() + " not found"));

        Project project = projectRepository.findById(requestDto.projectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID: " + requestDto.projectId() + " not found"));

        Task updatedTask = Task.builder()
                .id(id)
                .title(requestDto.title())
                .description(requestDto.description())
                .status(requestDto.status())
                .dueDate(requestDto.dueDate())
                .users(users)
                .project(project)
                .build();

        Task savedTask = taskRepository.save(updatedTask);
        logAudit("Update Task", String.valueOf(savedTask.getId()), savedTask.getTitle(), savedTask);

        return objectMapper.convertValue(savedTask, TaskResponseDto.class);
    }

    @Override
    public TaskResponseDto getTaskById(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID: " + id + " not found"));

        logAudit("Get Task By ID", String.valueOf(task.getId()), task.getTitle(), task);

        return objectMapper.convertValue(task, TaskResponseDto.class);
    }

    @Override
    public Page<TaskResponseDto> getAllTasks(int pageNumber, String sortBy) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        Page<Task> tasks = taskRepository.findAll(pageable);

        logAudit("Get All Tasks", "PAGE_" + pageNumber, "None", "Task");

        return tasks.map(task -> objectMapper.convertValue(task, TaskResponseDto.class));
    }

    @Override
    public Page<TaskResponseDto> getOverdueTasks(int pageNumber, String sortBy) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        Page<Task> tasks = taskRepository.findAllOverdueTasks(Date.valueOf(LocalDate.now()),pageable);

        logAudit("Get All Overdue Tasks", "PAGE_" + pageNumber, "None", "Task");

        return tasks.map(task -> objectMapper.convertValue(task, TaskResponseDto.class));
    }

    @Override
    public List<StatusCountDto> countTasksGroupedByStatus() {
        List<StatusCountProjection> statusCountProjections = taskRepository.countTasksGroupedByStatus();

        return statusCountProjections
                .stream()
                .map(statusCount -> objectMapper.convertValue(statusCount, StatusCountDto.class))
                .collect(Collectors.toList());
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
