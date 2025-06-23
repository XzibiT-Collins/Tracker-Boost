package com.project.tracker.authentication.oauth2Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tracker.authentication.jwtService.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        System.out.println("CustomOauthSuccessHandler called onAuthenticationSuccess");
        //get user from the authentication object
        OidcUser oAuth2User =(OidcUser) authentication.getPrincipal();
        System.out.println("Authenticated user: "+ oAuth2User.getFullName());
        String email = oAuth2User.getEmail();

        //generate signed token
        String token = jwtService.generateToken(email);
        System.out.println("Generated Token: " + token + " for user: " + email + " with role: " + authentication.getAuthorities() +"\n");
        //return the generated token to the client
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(Map.of(
                        "token", token,
                        "email", email
                ))
        );
    }
}
