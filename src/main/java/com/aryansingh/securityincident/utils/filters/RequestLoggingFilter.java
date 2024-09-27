package com.aryansingh.securityincident.utils.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger apiLogger = LoggerFactory.getLogger("API_REQUEST_LOGGER");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String ipAddress = request.getRemoteAddr();
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        LocalDateTime timestamp = LocalDateTime.now();

        apiLogger.info("Timestamp: {}, IP Address: {}, HTTP Method: {}, Endpoint: {}",
                timestamp, ipAddress, httpMethod, requestURI);

        filterChain.doFilter(request, response);
    }
}
