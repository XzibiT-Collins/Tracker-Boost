package com.project.tracker.utils;

import com.project.tracker.repositories.TaskRepository;
import com.project.tracker.repositories.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("accessChecker")
public class AccessChecker {
    private TaskRepository taskRepository;
    private UsersRepository usersRepository;

    public AccessChecker(TaskRepository taskRepository, UsersRepository usersRepository) {
        this.taskRepository = taskRepository;
        this.usersRepository = usersRepository;
    }

    public boolean isOwnerOfTask(int taskId, String email){
        return taskRepository.findById(taskId)
                .map(task -> task.getUsers().getEmail().equals(email))
                .orElse(false);
    }

    public boolean isOwnerAdmin(int taskId, Authentication authentication){
        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role ->
                        role.toString().equalsIgnoreCase("ROLE_ADMIN"));

        return isAdmin || isOwnerOfTask(taskId, email);
    }
}
