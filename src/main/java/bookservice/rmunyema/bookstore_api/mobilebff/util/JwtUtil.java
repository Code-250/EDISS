package bookservice.rmunyema.bookstore_api.mobilebff.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Component
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

    @Value("${jwt.issuer}")
    private String expectedIssuer;

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            // Decode the payload (second part of the token)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payloadJson = objectMapper.readTree(payload);

            // Check subject
            if (!payloadJson.has("sub") || !VALID_SUBJECTS.contains(payloadJson.get("sub").asText())) {
                return false;
            }

            // Check expiration
            if (!payloadJson.has("exp")) {
                return false;
            }
            long expiration = payloadJson.get("exp").asLong();
            if (expiration * 1000 < System.currentTimeMillis()) {
                return false;
            }

            // Check issuer
            if (!payloadJson.has("iss") || !expectedIssuer.equals(payloadJson.get("iss").asText())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}