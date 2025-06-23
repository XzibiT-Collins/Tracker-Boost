package com.project.tracker.dto.responseDto;

import lombok.Builder;

@Builder
public record StatusCountDto(
    String status,
    long count
){}
