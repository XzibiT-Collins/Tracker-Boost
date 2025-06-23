package com.project.tracker.authentication.customFilters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.tracker.authentication.jwtService.JwtService;
import com.project.tracker.exceptions.globalExceptions.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final Logger logger = Logger.getLogger(JwtFilter.class.getName());
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService,
                     UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                logger.info("Token found: " + token);
                userName = jwtService.extractUserName(token);
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response); // continue normally

        } catch (ExpiredJwtException e) {
            logger.severe("Expired JWT: " + e.getMessage());
            sendErrorResponse(response, "Token expired: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);

        } catch (JwtException | IllegalArgumentException e) {
            logger.severe("JWT Error: " + e.getMessage());
            sendErrorResponse(response, "Invalid token: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);

        } catch (Exception e) {
            logger.severe("Unexpected Error: " + e.getMessage());
            sendErrorResponse(response, "Authentication error: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .message(message)
                .timestamp(new Date())
                .build();

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}
