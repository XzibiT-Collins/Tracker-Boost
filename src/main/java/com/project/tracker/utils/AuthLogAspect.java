package com.project.tracker.utils;

import com.project.tracker.authentication.jwtService.JwtService;
import com.project.tracker.models.authmodels.AuthLog;
import com.project.tracker.repositories.AuthLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@Slf4j
public class AuthLogAspect {

    private final JwtService jwtService;
    private final AuthLogRepository authLogRepository;

    public AuthLogAspect(JwtService jwtService,AuthLogRepository authLogRepository) {
        this.jwtService = jwtService;
        this.authLogRepository = authLogRepository;
    }

    @Pointcut("execution(* com.project.tracker.authentication.oauth2Service.CustomOidcUserService.loadUser(..))")
    public void authenticationMethods() {}

    @AfterReturning(pointcut = "authenticationMethods()", returning = "result")
    public void logSuccess(JoinPoint joinPoint, Object result) {
        String email = extractEmailFromArgs(joinPoint);
        saveLog(email, "LOGIN_SUCCESS", "Authentication successful");
    }

    @AfterThrowing(pointcut = "authenticationMethods()", throwing = "ex")
    public void logFailure(JoinPoint joinPoint, Throwable ex) {
        String email = extractEmailFromArgs(joinPoint);
        saveLog(email, "LOGIN_FAILURE", ex.getMessage());
    }

    private void saveLog(String email, String action, String message) {
        AuthLog log = AuthLog.builder()
                .email(email)
                .action(action)
                .message(message)
                .timestamp(new Date())
                .build();

        authLogRepository.save(log);
    }

    private String extractEmailFromArgs(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletRequest request) {
                String token = request.getHeader("Authorization");
                return extractEmailFromToken(token);
            } else if (arg instanceof OidcUserRequest oidcRequest) {
                Object email = oidcRequest.getAdditionalParameters().get("email");
                return email != null ? email.toString() : "UNKNOWN";
            }
        }
        return "UNKNOWN";
    }


    private String extractEmailFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);

            try {
                return jwtService.extractUserName(jwt);
            } catch (Exception e) {
                log.error("Error decoding token: {}", e.getMessage());
            }
        }
        return "UNKNOWN";
    }
}
