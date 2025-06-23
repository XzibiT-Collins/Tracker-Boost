package com.project.tracker.controllers;

import com.project.tracker.dto.responseDto.AuditLogResponseDto;
import com.project.tracker.services.serviceInterfaces.AuditLogService;
import com.project.tracker.sortingEnums.AuditLogSorting;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    protected ResponseEntity<Page<AuditLogResponseDto>> getAllAuditLogs(
            @RequestParam(defaultValue = "SORT_BY_TIMESTAMP")
            AuditLogSorting sortBy, @RequestParam(defaultValue = "0") int pageNumber){

        return ResponseEntity
                .ok()
                .body(auditLogService.getAllAuditLogs(sortBy.getField(),pageNumber));

    }
}
