package com.bookstore.mobilebff.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class JwtUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Set<String> VALID_SUBJECTS = new HashSet<>();

    static {
        VALID_SUBJECTS.add("starlord");
        VALID_SUBJECTS.add("gamora");
        VALID_SUBJECTS.add("drax");
        VALID_SUBJECTS.add("rocket");
        VALID_SUBJECTS.add("groot");
    }

    @Value("${jwt.issuer:cmu.edu}")
    private String expectedIssuer;

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