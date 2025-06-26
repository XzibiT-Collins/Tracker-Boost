package com.project.tracker.authentication.oauth2Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tracker.authentication.jwtService.JwtService;
import com.project.tracker.dto.responseDto.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomOauthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public CustomOauthSuccessHandler(JwtService jwtService,
                                     ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OidcUser oAuth2User =(OidcUser) authentication.getPrincipal();
        String email = oAuth2User.getEmail();

        String token = jwtService.generateToken(email);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(ApiResponseDto.success(Map.of(
                        "token", token,
                        "email", email
                ), HttpStatus.OK.value()))
        );
    }
}
