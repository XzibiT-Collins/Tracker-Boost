package com.project.tracker.repositories;

import com.project.tracker.models.authmodels.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
