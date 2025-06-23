package com.project.tracker.repositories;

import com.project.tracker.models.Task;
import com.project.tracker.services.serviceInterfaces.StatusCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Page<Task> findAll(Pageable pageable);

    //Overdue tasks
    @Query("SELECT t FROM Task t WHERE t.dueDate < :currentDate")
    Page<Task> findAllOverdueTasks(@Param("currentDate") Date currentDate,
                                   Pageable pageable);

    // Tasks count by status
    @Query("SELECT t.status AS status, COUNT(t) AS count FROM Task t GROUP BY t.status")
    List<StatusCountProjection> countTasksGroupedByStatus();
}
