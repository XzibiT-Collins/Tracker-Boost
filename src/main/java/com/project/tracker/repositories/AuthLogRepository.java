package com.project.tracker.repositories;

import com.project.tracker.models.authmodels.AuthLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthLogRepository extends MongoRepository <AuthLog, String>{

}
