package com.project.tracker.services.serviceInterfaces;

import com.project.tracker.dto.requestDto.TaskRequestDto;
import com.project.tracker.dto.responseDto.StatusCountDto;
import com.project.tracker.dto.responseDto.TaskResponseDto;
import com.project.tracker.models.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    TaskResponseDto addTask(TaskRequestDto requestDto);
    void deleteTask(int id);
    TaskResponseDto updateTask(int id,TaskRequestDto requestDto);
    TaskResponseDto getTaskById(int id);
    Page<TaskResponseDto> getAllTasks(int pageNumber, String sortBy);
    Page<TaskResponseDto> getOverdueTasks(int pageNumber, String sortBy);
    List<StatusCountDto> countTasksGroupedByStatus();
}
