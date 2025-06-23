package com.project.tracker.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.tracker.models.authmodels.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Role role;

    private Set<String> skills;

    @OneToMany(mappedBy = "users")
    @JsonManagedReference(value = "users-task")
    private List<Task> tasks;
}
