package com.project.tracker.controllers.oauthController;

import com.project.tracker.dto.responseDto.OauthSuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/success")
public class OauthSuccessController {

    @GetMapping("/")
    protected ResponseEntity<OauthSuccessResponse> oauthSuccess(String token, String email){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OauthSuccessResponse.builder()
                        .email(email)
                        .token(token)
                .build());
    }
}
