package com.project.tracker.repositories;


import com.project.tracker.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Page<Users> findAll(Pageable pageable);

    //Top 5 developers with most tasks
    @Query("SELECT d FROM Users d LEFT JOIN d.tasks t GROUP BY d ORDER BY COUNT(t) DESC")
    Page<Users> findTop5ByOrderByTasksCountDesc(Pageable pageable);

    Users findByEmail(String email);

    boolean existsByEmail(String email);
}
