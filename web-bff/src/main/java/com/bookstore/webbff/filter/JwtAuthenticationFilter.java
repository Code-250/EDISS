package com.bookstore.webbff.filter;

import com.bookstore.webbff.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter is a filter that checks for the presence of a JWT
 * token
 * in the Authorization header of incoming requests.
 * It validates the token and allows or denies access to the requested resource.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * The JwtUtil instance used to validate JWT tokens.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /*
     * This method is called for every incoming request.
     * It checks for the presence of a JWT token in the Authorization header,
     * validates the token, and allows or denies access to the requested resource.
     * If the token is valid, the request is allowed to proceed.
     * If the token is invalid or missing, a 401 Unauthorized response is sent.
     * The status endpoint is skipped to allow health checks without authentication.
     * 
     * @param request The incoming HTTP request.
     * 
     * @param response The HTTP response to be sent back to the client.
     * 
     * @param filterChain The filter chain to continue processing the request.
     * 
     * @throws ServletException If an error occurs during request processing.
     * 
     * @throws IOException If an I/O error occurs during request processing.
     * 
     * @see OncePerRequestFilter#doFilterInternal(HttpServletRequest,
     * HttpServletResponse, FilterChain)
     * 
     * @see JwtUtil#validateToken(String)
     * 
     * @see HttpServletRequest#getHeader(String)
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