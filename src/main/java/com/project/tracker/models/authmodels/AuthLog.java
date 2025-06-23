package com.project.tracker.models.authmodels;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Setter
@Getter
@Document(collection = "auth_logs")
public class AuthLog {
    @Id
    private ObjectId id;
    private String email;
    private String action;
    private String message;
    private Date timestamp = new Date();
}
