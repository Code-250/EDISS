package com.bookstore.webbff.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

/**
 * JwtUtil is a utility class for validating JWT tokens.
 * It checks the token's structure, expiration, subject, and issuer.
 */
@Component
@Slf4j
public class JwtUtil {

    /**
     * ObjectMapper instance for JSON processing.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Set<String> VALID_SUBJECTS = new HashSet<>();

    /**
     * Static block to initialize valid subjects.
     * This block runs once when the class is loaded.
     */
    static {
        VALID_SUBJECTS.add("starlord");
        VALID_SUBJECTS.add("gamora");
        VALID_SUBJECTS.add("drax");
        VALID_SUBJECTS.add("rocket");
        VALID_SUBJECTS.add("groot");
    }

    /**
     * The expected issuer of the JWT token.
     */
    @Value("${jwt.issuer:cmu.edu}")
    private String expectedIssuer;

    /**
     * Validates the JWT token.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                log.info("Token is not complete!");
                return false;
            }

            // Decode the payload (second part of the token)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payloadJson = objectMapper.readTree(payload);

            // Check subject
            if (!payloadJson.has("sub") || !VALID_SUBJECTS.contains(payloadJson.get("sub").asText())) {
                log.info("Subject is not valid!");
                return false;
            }

            // Check expiration
            if (!payloadJson.has("exp")) {
                log.info("No exp specified");
                return false;
            }
            long expiration = payloadJson.get("exp").asLong();
            if (expiration < System.currentTimeMillis() / 1000) {
                log.info("Token is expired!");
                return false;
            }

            // Check issuer
            if (!payloadJson.has("iss") || !expectedIssuer.equals(payloadJson.get("iss").asText())) {
                log.info("Issuer is not valid!");
                return false;
            }

            return true;
        } catch (Exception e) {
            log.info("Error while validating token {}", e.getMessage());
            return false;
        }
    }
}