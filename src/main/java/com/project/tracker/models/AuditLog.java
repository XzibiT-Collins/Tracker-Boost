package com.project.tracker.models;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Builder
@Document(collection = "audit_log")
public class AuditLog {
    @Id
    private ObjectId id;
    private String actionType;
    private String entityId;
    private String actorName;
    private String payload;
    private Date timestamp = new Date();
}
