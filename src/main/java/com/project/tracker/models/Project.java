package com.project.tracker.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.tracker.sortingEnums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;


import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String projectName;
    private String description;
    private Date deadline;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "project-task")
    private List<Task> tasks;
}
