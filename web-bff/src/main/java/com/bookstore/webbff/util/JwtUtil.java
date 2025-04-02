package com.bookstore.webbff.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    private static final List<String> VALID_SUBS = Arrays.asList("starlord", "gamora", "drax", "rocket", "groot");
    private static final String VALID_ISS = "emu.edu";

    public static Claims validateJwt(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String sub = claims.getSubject();
            if (sub == null || !VALID_SUBS.contains(sub)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid 'sub' claim");
            }

            Date exp = claims.getExpiration();
            if (exp == null || exp.before(new Date())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
            }

            String iss = claims.getIssuer();
            if (iss == null || !iss.equals(VALID_ISS)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid 'iss' claim");
            }

            return claims;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }
    }
}