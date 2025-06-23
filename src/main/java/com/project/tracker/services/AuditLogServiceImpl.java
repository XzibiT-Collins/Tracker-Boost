package com.project.tracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tracker.dto.responseDto.AuditLogResponseDto;
import com.project.tracker.models.AuditLog;
import com.project.tracker.repositories.AuditLogRepository;
import com.project.tracker.services.serviceInterfaces.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl implements AuditLogService {
    private AuditLogRepository auditLogRepository;
    private ObjectMapper objectMapper;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addAuditLog(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }

    @Override
    public Page<AuditLogResponseDto> getAllAuditLogs(String sortBy, int pageNumber) {
        //sort
        Sort sort = Sort.by(sortBy);
        //paginate by
        int paginateBy = 10;

        //pageable
        Pageable pageableObj = PageRequest.of(pageNumber, paginateBy, sort);

        Page<AuditLog> auditLogs = auditLogRepository.findAll(pageableObj);

        return auditLogs
                .map(auditLog -> objectMapper.convertValue
                        (auditLog, AuditLogResponseDto.class));
    }
}
