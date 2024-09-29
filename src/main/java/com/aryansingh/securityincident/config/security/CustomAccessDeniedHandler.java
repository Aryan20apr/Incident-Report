package com.aryansingh.securityincident.config.security;

import com.aryansingh.securityincident.utils.ApiResponse;
import com.aryansingh.securityincident.utils.InsufficientRolesException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)  {
        log.info("Access denied: " + accessDeniedException.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        // Create a custom response body
        ApiResponse<String> apiResponse = new ApiResponse<>(
                "Access Denied",
                "You do not have permission to access this resource."
        );

        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

