package com.project.tracker.utils;

import com.project.tracker.models.authmodels.Role;
import com.project.tracker.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleInitializer implements CommandLineRunner {

    RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    //Populate the database with roles
    @Override
    public void run(String... args) throws Exception {
        List<String> roles = List.of("ROLE_ADMIN","ROLE_MANAGER","ROLE_DEVELOPER","ROLE_CONTRACTOR");

        for(String role : roles){
            Role dbRole= roleRepository.findByName(role);
            if(dbRole==null){
                roleRepository.save(Role.builder().name(role).build());
            }
        }
    }
}
