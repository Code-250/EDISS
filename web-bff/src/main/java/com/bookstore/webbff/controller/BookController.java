package com.bookstore.webbff.controller;

import com.bookstore.webbff.dto.BookDTO;
import com.bookstore.webbff.dto.RecommendationResponseDto;
import com.bookstore.webbff.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;

/**
 * BookController is a REST controller that handles HTTP requests related to
 * books.
 * It provides endpoints to get, add, and update book information.
 */
@RestController
@Slf4j
@Tag(name = "Web BFF API Book", description = "Backend for Frontend API for web clients")
public class BookController {

    /**
     * The BookService instance used to handle book-related operations.
     */
    @Autowired
    private BookService bookService;

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return A ResponseEntity containing the BookDTO if found, or an error
     *         response if not found.
     */
    @GetMapping({ "/books/isbn/{isbn}", "/books/{isbn}" })
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        try {
            BookDTO foundBook = bookService.getBook(isbn).getBody();
            return ResponseEntity.status(HttpStatus.OK).body(foundBook);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Retrieves related books by its ISBN.
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return A ResponseEntity containing the BookDTO if found, or an error
     *         response if not found.
     */
    @GetMapping({ "/books/{isbn}/related-books" })
    public ResponseEntity<List<RecommendationResponseDto>> getRelatedBooks(@PathVariable String isbn) {
        try {

            ResponseEntity<List<RecommendationResponseDto>> foundBook = bookService.getRelatedBooks(isbn);
            if (foundBook.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(foundBook.getBody());
        } catch (HttpServerErrorException.GatewayTimeout e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        } catch (HttpServerErrorException.ServiceUnavailable e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Adds a new book to the system.
     *
     * @param bookDTO The BookDTO containing the details of the book to add.
     * @return A ResponseEntity containing the created BookDTO or an error response
     *         if the book already exists.
     */
    @PostMapping("/cmd/books")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        try {
            ResponseEntity<?> createdBook = bookService.createBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook.getBody());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("message", "This ISBN already exists in the system."));
        } catch (HttpClientErrorException.BadRequest e) {
            log.info("Bad request " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (HttpServerErrorException.InternalServerError e) {
            log.info("Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing book in the system.
     *
     * @param isbn    The ISBN of the book to update.
     * @param bookDTO The BookDTO containing the updated details of the book.
     * @return A ResponseEntity containing the updated BookDTO or an error response
     *         if the book is not found.
     */
    @PutMapping("/cmd/books/{isbn}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @Valid @RequestBody BookDTO bookDTO) {
        try {
            ResponseEntity<BookDTO> updatedBook = bookService.updateBook(isbn, bookDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedBook.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.BadRequest e) {
            log.info("Bad request " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /**
     * Endpoint for searching books by keyword
     * @param keyword The search term provided as a request parameter
     * @return List of books matching the search criteria
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String keyword) {
        log.info("Received request to search books with keyword: {}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("Empty keyword provided for search");
            return ResponseEntity.badRequest().build();
        }

        try {
            // Call the service method that makes the actual API call
            return bookService.searchBooksByKeyword(keyword);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.info("No books found matching keyword: {}", keyword);
                return ResponseEntity.noContent().build();
            }
            log.error("Error from book service: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("Error processing search request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}