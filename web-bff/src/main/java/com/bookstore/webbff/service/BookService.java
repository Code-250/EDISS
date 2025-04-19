package com.bookstore.webbff.service;

import com.bookstore.webbff.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * BookService is a service class that handles operations related to books.
 * It interacts with the backend service to perform CRUD operations on book
 * data.
 */
@Service
public class BookService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    /**
     * Constructor for BookService.
     *
     * @param restTemplate The RestTemplate instance used for making HTTP requests.
     * @param baseUrl      The base URL of the backend service.
     */
    @Autowired
    public BookService(RestTemplate restTemplate,
            @Value("http://book-service:3000") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return A ResponseEntity containing the BookDTO if found, or an error
     *         response if not found.
     */
    public ResponseEntity<BookDTO> getBook(String isbn) {
        try {
            return restTemplate.getForEntity(baseUrl + "/books/" + isbn, BookDTO.class);
        } catch (HttpClientErrorException e) {
            throw e; // Re-throw to let controller handle it
        } catch (Exception e) {
            throw new RuntimeException("Error creating book", e);
        }

    }

    /**
     * Retrieves related books by its ISBN.
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return A ResponseEntity containing the BookDTO if found, or an error
     *         response if not found.
     */
    public ResponseEntity<BookDTO> getRelatedBooks(String isbn) {
        try {
            return restTemplate.getForEntity(baseUrl + "/books/" + isbn + "/related-books", BookDTO.class);
        } catch (HttpClientErrorException e) {
            throw e; // Re-throw to let controller handle it
        } catch (Exception e) {
            throw new RuntimeException("Error creating book", e);
        }

    }

    /**
     * Creates a new book.
     *
     * @param bookDTO The BookDTO containing the details of the book to create.
     * @return A ResponseEntity containing the created BookDTO or an error response
     *         if the book already exists.
     */
    public ResponseEntity<BookDTO> createBook(BookDTO bookDTO) {
        try {
            return restTemplate.postForEntity(baseUrl + "/books", bookDTO, BookDTO.class);
        } catch (HttpClientErrorException e) {
            throw e; // Re-throw to let controller handle it
        } catch (Exception e) {
            throw new RuntimeException("Error creating book", e);
        }
    }

    /**
     * Updates an existing book.
     *
     * @param isbn    The ISBN of the book to update.
     * @param bookDTO The BookDTO containing the updated details of the book.
     * @return A ResponseEntity containing the updated BookDTO or an error response
     *         if the book does not exist.
     */
    public ResponseEntity<BookDTO> updateBook(String isbn, BookDTO bookDTO) {
        try {
            return restTemplate.exchange(
                    baseUrl + "/books/" + isbn,
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
