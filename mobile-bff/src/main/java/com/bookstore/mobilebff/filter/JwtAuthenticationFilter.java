package com.bookstore.mobilebff.filter;

import com.bookstore.mobilebff.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter.java
 * This class is a Spring Boot filter that intercepts HTTP requests to check for
 * JWT authentication.
 * It extends OncePerRequestFilter to ensure that the filter is executed once
 * per
 * request.
 * The filter checks for the presence of a valid JWT token in the Authorization
 * header.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * doFilterInternal() method is called for each HTTP request.
     * It checks for the presence of a JWT token in the Authorization header and
     * validates it.
     * If the token is valid, the request is allowed to proceed; otherwise, an
     * unauthorized response is returned.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain to continue processing the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip status endpoint
        if (request.getRequestURI().equals("/status")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Check for Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header with Bearer token is required");
            return;
        }

        // Extract and validate the token
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}