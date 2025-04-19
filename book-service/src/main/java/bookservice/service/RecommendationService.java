package bookservice.service;

import bookservice.entity.Book;
import bookservice.entity.RelatedBook;
import bookservice.repository.BookRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Service
public class RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    private static final int TIMEOUT_SECONDS = 3;
    private static final String CIRCUIT_BREAKER_NAME = "recommendationService";

    @Autowired
    private BookRepository bookRepository;

    private String recommendationServiceUrl = "http://18.118.230.221";

    private final RestTemplate restTemplate;

    public RecommendationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS)).readTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
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
    public CompletableFuture<?> getRelatedBooks(String isbn) {
        return CompletableFuture.supplyAsync(() -> {
            // First check if the book exists
            Optional<Book> bookOpt = bookRepository.findById(isbn);
            if (bookOpt.isEmpty()) {
                logger.info("Book with ISBN {} not found in database", isbn);
                return List.of(); // Return empty list, will result in 204 response
            }

            try {
                String url = recommendationServiceUrl + "/recommendations?isbn=" + isbn;
                logger.info("Calling recommendation service: {}", url);

                try {
                    ResponseEntity<RelatedBook[]> response = restTemplate.getForEntity(url, RelatedBook[].class);

                    RelatedBook[] books = response.getBody();
                    return books != null ? enrichRecommendations(books) : List.of();

                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        logger.info("Recommendation service returned 404 for ISBN: {}", isbn);
                        return List.of(); // Return empty list, will result in 204 response
                    }
                    throw e;
                }

            } catch (Exception e) {
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
     * Enhances the recommendation data by filling in any missing details
     * from our local database when possible
     */
    private List<RelatedBook> enrichRecommendations(RelatedBook[] recommendations) {
        List<RelatedBook> enrichedBooks = new ArrayList<>();

        for (RelatedBook relatedBook : recommendations) {
            String isbn = relatedBook.getISBN();

            // If we have this book in our database, use our data
            Optional<Book> bookOpt = bookRepository.findById(isbn);
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();

                // Only update fields if they're missing or empty in the recommendation
                if (relatedBook.getTitle() == null || relatedBook.getTitle().isEmpty()) {
                    relatedBook.setTitle(book.getTitle());
                }

                if (relatedBook.getAuthor() == null || relatedBook.getAuthor().isEmpty()) {
                    relatedBook.setAuthor(book.getAuthor());
                }
            }

            enrichedBooks.add(relatedBook);
        }

        return enrichedBooks;
    }

    public static class CircuitOpenException extends Exception {
        public CircuitOpenException(String message) {
            super(message);
        }
    }
}