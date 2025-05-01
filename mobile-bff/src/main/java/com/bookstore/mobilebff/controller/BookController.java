package com.bookstore.mobilebff.controller;

import com.bookstore.mobilebff.dto.BookDTO;
import com.bookstore.mobilebff.dto.RecommendationResponseDto;
import com.bookstore.mobilebff.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * BookController.java
 * This class is a Spring Boot REST controller that handles HTTP requests related to books.
 * It provides endpoints to get, add, and update book information.
 * The controller interacts with the BookService to perform operations on book data.
 * It uses Lombok for logging and validation annotations for request body validation.
 * The controller also handles exceptions and returns appropriate HTTP responses.
 */
@RestController
@Slf4j
@Tag(name = "Mobile BFF API", description = "Backend for Frontend API for mobile clients")
public class BookController {

    /*
     * BookService is a service class that provides methods to interact with book
     * data.
     * It is injected into the controller using Spring's @Autowired annotation.
     */
    @Autowired
    private BookService bookService;

    /*
     * getAllBooks() method handles GET requests to retrieve all books.
     * It returns a ResponseEntity containing a list of books and an HTTP status
     * code.
     * 
     * @Param page The page number for pagination.
     * 
     * @Param size The number of books per page.
     * 
     * @Param sort The sorting criteria for the books.
     * 
     * @Param genre The genre of the books to filter.
     * 
     * @Param author The author of the books to filter.
     * 
     * @return ResponseEntity containing a list of books and HTTP status code.
     * 
     * @throws HttpClientErrorException.BadRequest if the request is bad.
     * 
     * @throws HttpClientErrorException.Unauthorized if the request is unauthorized.
     * 
     * @throws HttpClientErrorException.NotFound if the books are not found.
     */
    @GetMapping({ "/books/isbn/{isbn}", "/books/{isbn}" })
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        try {
            ResponseEntity<BookDTO> foundBook = bookService.getBook(isbn);
            if (foundBook != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("title", foundBook.getBody().getTitle());
                response.put("description", foundBook.getBody().getDescription());

                // // Convert genre to integer if it's "3" or non-fiction
                // if ("non-fiction".equalsIgnoreCase(foundBook.getBody().getGenre())
                // || "3".equals(foundBook.getBody().getGenre())) {
                // response.put("genre", 3); // This will be an integer in the JSON
                // } else {
                // response.put("genre", 1);
                // }
                response.put("genre", foundBook.getBody().getGenre());
                response.put("price", foundBook.getBody().getPrice());
                response.put("quantity", foundBook.getBody().getQuantity());
                response.put("ISBN", foundBook.getBody().getIsbn());
                response.put("Author", foundBook.getBody().getAuthor());

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*
     * addBook() method handles POST requests to add a new book.
     * It takes a BookDTO object as input and returns a ResponseEntity with the
     * created book information.
     *
     * @param bookDTO The BookDTO object containing book details.
     *
     * @return ResponseEntity containing the created book information and HTTP
     * status code.
     *
     * @throws HttpClientErrorException.UnprocessableEntity if the request is
     * unprocessable.
     *
     * @throws HttpClientErrorException.BadRequest if the request is bad.
     *
     * @throws HttpClientErrorException.Unauthorized if the request is unauthorized.
     *
     * @Valid annotation is used to validate the request body against the
     * constraints defined in the BookDTO class.
     * The @RequestBody annotation binds the request body to the method parameter.
     * The method returns a ResponseEntity with the created book information and an
     * HTTP status code.
     * The method handles exceptions and returns appropriate HTTP responses.
     * The method uses a try-catch block to handle exceptions that may occur during
     * the book creation process.
     */
    @PostMapping("/cmd/books")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        try {
            ResponseEntity<BookDTO> createdBook = bookService.createBook(bookDTO);
            createdBook.getBody().setIsbn(bookDTO.getIsbn());
            Map<String, Object> response = new HashMap<>();
            response.put("title", createdBook.getBody().getTitle());
            response.put("description", createdBook.getBody().getDescription());

            // Convert genre to integer if it's "3" or non-fiction
            response.put("genre", createdBook.getBody().getGenre());
            response.put("price", createdBook.getBody().getPrice());
            response.put("quantity", createdBook.getBody().getQuantity());
            response.put("ISBN", createdBook.getBody().getIsbn());
            response.put("Author", createdBook.getBody().getAuthor());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
     * updateBook() method handles PUT requests to update an existing book.
     * It takes the ISBN of the book and a BookDTO object as input and returns a
     * ResponseEntity with the updated book information.
     * 
     * @param isbn The ISBN of the book to be updated.
     * 
     * @param bookDTO The BookDTO object containing updated book details.
     * 
     * @return ResponseEntity containing the updated book information and HTTP
     * status code.
     * 
     * @throws HttpClientErrorException.NotFound if the book is not found.
     * 
     * @throws HttpClientErrorException.BadRequest if the request is bad.
     * 
     * @throws HttpClientErrorException.Unauthorized if the request is unauthorized.
     */
    @PutMapping("/cmd/books/{isbn}")
    public ResponseEntity<?> updateBook(@PathVariable String isbn, @Valid @RequestBody BookDTO bookDTO) {
        try {
            ResponseEntity<BookDTO> updatedBook = bookService.updateBook(isbn, bookDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("title", updatedBook.getBody().getTitle());
            response.put("description", updatedBook.getBody().getDescription());

            // Convert genre to integer if it's "3" or non-fiction
            response.put("genre", updatedBook.getBody().getGenre());
            response.put("price", updatedBook.getBody().getPrice());
            response.put("quantity", updatedBook.getBody().getQuantity());
            response.put("ISBN", updatedBook.getBody().getIsbn());
            response.put("Author", updatedBook.getBody().getAuthor());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Get related books", description = "Retrieves a list of books related to the specified ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Related books found"),
            @ApiResponse(responseCode = "204", description = "No related books found"),
            @ApiResponse(responseCode = "503", description = "Circuit breaker is open"),
            @ApiResponse(responseCode = "504", description = "Recommendation service timed out")
    })
    @GetMapping("/books/{isbn}/related-books")
    public ResponseEntity<List<RecommendationResponseDto>> getRelatedBooks(
            @Parameter(description = "ISBN of the book to get recommendations for") @PathVariable String isbn) {
        try {

            ResponseEntity<List<RecommendationResponseDto>> foundBook = bookService.getRelatedBooks(isbn);
            if(foundBook.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
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