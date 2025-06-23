package com.project.tracker.services.serviceInterfaces;

import com.project.tracker.dto.responseDto.AuditLogResponseDto;
import com.project.tracker.models.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AuditLogService {
    void addAuditLog(AuditLog auditLog);
    Page<AuditLogResponseDto> getAllAuditLogs(String sortBy, int pageNumber);
}
