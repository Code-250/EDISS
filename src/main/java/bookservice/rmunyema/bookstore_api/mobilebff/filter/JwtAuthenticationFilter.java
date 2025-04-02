package bookservice.rmunyema.bookstore_api.mobilebff.filter;

import bookservice.rmunyema.bookstore_api.mobilebff.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip status endpoint
        if (request.getRequestURI().equals("/status")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check for X-Client-Type header
        String clientType = request.getHeader("X-Client-Type");
        if (clientType == null || clientType.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("X-Client-Type header is required");
            return;
        }

        // For mobile BFF, verify the client type is iOS or Android
        if (!clientType.equals("iOS") && !clientType.equals("Android")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid X-Client-Type for Mobile BFF. Expected iOS or Android");
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