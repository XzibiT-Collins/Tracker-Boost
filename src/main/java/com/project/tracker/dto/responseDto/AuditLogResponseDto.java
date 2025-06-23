package com.project.tracker.dto.responseDto;

import lombok.Getter;

public record AuditLogResponseDto(
        String id,
        String actionType,
        String entityId,
        String actorName,
        String payload
) {}
