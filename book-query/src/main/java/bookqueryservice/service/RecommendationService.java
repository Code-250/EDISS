package bookqueryservice.service;

import bookqueryservice.dto.RecommendationDto;
import bookqueryservice.dto.RecommendationResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * This class is a Spring Boot service that handles operations related to
 * recommendations.
 * It interacts with a recommendation engine to retrieve related books based on
 * ISBN.
 */
@Service
public class RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    // Timeout for the recommendation service in seconds
    // This is the timeout for the RestTemplate
    private static final int TIMEOUT_SECONDS = 3;
    // Name of the circuit breaker
    private static final String CIRCUIT_BREAKER_NAME = "recommendationService";

    // URL of the recommendation service
    @Value("${recommendation.service.url}")
    private String recommendationServiceUrl;

    private final RestTemplate restTemplate;

    /**
     * Constructor for RecommendationService.
     * It initializes the RestTemplate with a timeout configuration.
     *
     * @param restTemplateBuilder The RestTemplateBuilder to create the RestTemplate
     *                            instance.
     */
    public RecommendationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .readTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }

    /**
     * Get related books from the recommendation engine with circuit breaker.
     * The Resilience4j annotations provide circuit breaking and timeout
     * functionality.
     *
     * @param isbn The ISBN of the book to get recommendations for
     * @return List of related books
     */
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "getRelatedBooksFallback")
    @TimeLimiter(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "getRelatedBooksFallback")
    public CompletableFuture<List<RecommendationResponseDto>> getRelatedBooks(String isbn) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Build the URL for the recommendation service
                String url = recommendationServiceUrl + "/recommended-titles/isbn/" + isbn;
                logger.info("Calling recommendation service: {}", url);

                // Make the request to the recommendation service
                ResponseEntity<RecommendationDto[]> response = restTemplate.getForEntity(url,
                        RecommendationDto[].class);

                // Check if we got a valid response with content
                if (response.getBody() == null) {
                    logger.info("Recommendation service returned null body for ISBN: {}", isbn);
                    return Collections.emptyList();
                }

                // Transform recommendations and exclude publisher information
                List<RecommendationResponseDto> recommendations = new ArrayList<>();
                for (RecommendationDto dto : response.getBody()) {
                    RecommendationResponseDto enriched = new RecommendationResponseDto();

                    // Set core fields, using fallbacks for missing values
                    enriched.setIsbn(dto.getIsbn() != null ? dto.getIsbn() : isbn);
                    enriched.setTitle(dto.getTitle() != null ? dto.getTitle() : "");
                    enriched.setAuthors(dto.getAuthors() != null ? dto.getAuthors() : "");

                    // Publisher field is explicitly excluded

                    recommendations.add(enriched);
                }

                return recommendations;

            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    logger.info("Recommendation service returned 404 for ISBN: {}", isbn);
                    return Collections.emptyList();
                }
                logger.error("HTTP client error from recommendation service: {}", e.getMessage());
                throw e;
            } catch (HttpServerErrorException.GatewayTimeout e) {
                logger.error("Error calling recommendation service", e);
                throw e;
            }
        });
    }

    /**
     * Fallback method called when the circuit breaker is open or the call times
     * out.
     *
     * @param isbn      The ISBN of the book
     * @param throwable The exception that triggered the fallback
     * @return Empty list wrapped in a CompletableFuture
     */
    public CompletableFuture<?> getRelatedBooksFallback(String isbn, Throwable throwable) {
        logger.warn("Fallback triggered for ISBN: {} due to: {}", isbn, throwable.getMessage());

        if (throwable instanceof TimeoutException) {
            // This is a timeout, throw a special exception that will be handled by the
            // controller
            return CompletableFuture.failedFuture(new TimeoutException("Recommendation service timed out"));
        } else {
            // This is a circuit breaker open event or other exception
            return CompletableFuture.failedFuture(new CircuitOpenException("Circuit breaker is open"));
        }
    }

    /**
     * Custom exception to indicate that the circuit is open.
     */
    public static class CircuitOpenException extends Exception {
        public CircuitOpenException(String message) {
            super(message);
        }
    }
}