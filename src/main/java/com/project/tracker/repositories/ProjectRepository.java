package com.project.tracker.repositories;

import com.project.tracker.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Page<Project> findAll(Pageable pageable);

//    Projects without any tasks
    @Query("SELECT p FROM Project p WHERE p.tasks IS EMPTY")
    Page<Project> findAllByTasksIsEmpty(Pageable pageable);
}
