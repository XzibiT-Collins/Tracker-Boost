package com.project.tracker.dto.responseDto;

import lombok.Builder;

@Builder
public record OauthSuccessResponse(String token, String email) {
}
