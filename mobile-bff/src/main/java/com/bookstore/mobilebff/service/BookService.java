package com.bookstore.mobilebff.service;

import com.bookstore.mobilebff.dto.BookDTO;
import com.bookstore.mobilebff.dto.RecommendationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * BookService.java
 * This class is a Spring Boot service that handles operations related to books.
 * It interacts with a backend service to perform CRUD operations on book data.
 * The service uses RestTemplate to make HTTP requests to the backend service.
 */
@Service
public class BookService {

    private final RestTemplate restTemplate;
    private final String commandUrl;
    private final String queryUrl;

    /**
     * Constructor for BookService.
     * It initializes the RestTemplate and base URL for the backend service.
     *
     * @param restTemplate The RestTemplate to make HTTP requests.
     * @param commandUrl      The base URL of the backend service.
     */
    @Autowired
    public BookService(RestTemplate restTemplate,
            @Value("http://book-command:3000") String commandUrl, @Value("http://book-query:3000") String queryUrl) {
        this.restTemplate = restTemplate;
        this.commandUrl = commandUrl;
        this.queryUrl = queryUrl;
    }

    /**
     * Get a book by its ISBN.
     * It makes a GET request to the backend service to retrieve the book
     * information.
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return ResponseEntity containing the book information and HTTP status code.
     */
    public ResponseEntity<BookDTO> getBook(String isbn) {
        try {
            return restTemplate.getForEntity(queryUrl + "/books/" + isbn, BookDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error getting book", e);
        }

    }

    /**
     * Get related books by its ISBN.
     * It makes a GET request to the backend service to retrieve the book
     * information.
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return ResponseEntity containing the book information and HTTP status code.
     */
    public ResponseEntity<List<RecommendationResponseDto>> getRelatedBooks(String isbn) {
        try {
            return restTemplate.exchange(
                    queryUrl + "/books/" + isbn + "/related-books",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RecommendationResponseDto>>() {
                    }
            );

        } catch (Exception e) {
            // Just rethrow with the same exception type for the controller to handle
             throw e;
        }

    }

    /**
     * Create a new book.
     * It makes a POST request to the backend service to create a new book.
     *
     * @param bookDTO The book information to create.
     * @return ResponseEntity containing the created book information and HTTP
     *         status code.
     */
    public ResponseEntity<BookDTO> createBook(BookDTO bookDTO) {
        try {

            return restTemplate.postForEntity(commandUrl + "/cmd/books", bookDTO, BookDTO.class);

        } catch (HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating book", e);
        }
    }

    /**
     * Update an existing book.
     * It makes a PUT request to the backend service to update the book information.
     *
     * @param isbn    The ISBN of the book to update.
     * @param bookDTO The updated book information.
     * @return ResponseEntity containing the updated book information and HTTP
     *         status code.
     */
    public ResponseEntity<BookDTO> updateBook(String isbn, BookDTO bookDTO) {
        try {
            return restTemplate.exchange(
                    commandUrl + "/cmd/books/" + isbn,
                    HttpMethod.PUT,
                    new HttpEntity<>(bookDTO),
                    BookDTO.class);

        } catch (HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating book", e);
        }

    }
}