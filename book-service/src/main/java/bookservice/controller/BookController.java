package bookservice.controller;

import bookservice.dto.BookDTO;
import bookservice.entity.Book;
import bookservice.entity.RelatedBook;
import bookservice.repository.BookRepository;
import bookservice.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * REST Controller for managing book resources.
 * Provides endpoints for creating, updating, and retrieving book information.
 */
@RestController
@RequestMapping("/books")
@Tag(name = "Book API", description = "Operations for managing books and retrieving recommendations")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RecommendationService recommendationService;

    /**
     * Creates a new book in the system.
     *
     * @param bookDTO The book data transfer object containing book details
     * @return ResponseEntity containing the created book DTO and Location header
     * @throws HttpStatus.UNPROCESSABLE_ENTITY (422) if the ISBN already exists
     * @throws HttpStatus.CREATED              (201) if the book is successfully
     *                                         created
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a book", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "422", description = "book Already Exists"),
    })
    @PostMapping
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        if (bookRepository.existsById(bookDTO.getIsbn())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("message", "This ISBN already exists in the system."));
        }
        Book book = new Book(bookDTO);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/books/" + savedBook.getIsbn())
                .body(new BookDTO(savedBook));
    }

    /**
     * Updates an existing book by its ISBN.
     *
     * @param isbn    The ISBN of the book to update
     * @param bookDTO The updated book data transfer object
     * @return ResponseEntity containing the updated book DTO
     * @throws HttpStatus.BAD_REQUEST (400) if the ISBN in the request body doesn't
     *                                match the path variable
     * @throws HttpStatus.NOT_FOUND   (404) if the book with the given ISBN doesn't
     *                                exist
     * @throws HttpStatus.OK          (200) if the book is successfully updated
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated book with provided ISBN", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "book found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @PutMapping("/{isbn}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @Valid @RequestBody BookDTO bookDTO) {
        if (bookDTO.getIsbn() == null || !bookDTO.getIsbn().equals(isbn)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!bookRepository.existsById(isbn)) {
            return ResponseEntity.notFound().build();
        }
        Book book = new Book(bookDTO);
        book.setIsbn(isbn);
        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(new BookDTO(updatedBook));
    }

    /**
     * Retrieves a book by its ISBN.
     * Supports two URL patterns: /books/isbn/{isbn} and /books/{isbn}
     *
     * @param isbn The ISBN of the book to retrieve
     * @return ResponseEntity containing the book DTO
     * @throws HttpStatus.OK        (200) if the book is found
     * @throws HttpStatus.NOT_FOUND (404) if the book doesn't exist
     */
    @Operation(summary = "Get a book for a specific ISBN", description = "Retrieves a book based on the provided ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully a book with Provided ISBN", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "book found"),
    })
    @GetMapping({ "/isbn/{isbn}", "/{isbn}" })
    public ResponseEntity<BookDTO> getBook(@PathVariable String isbn) {
        return bookRepository.findById(isbn)
                .map(book -> ResponseEntity.ok(new BookDTO(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Checks the status of the service.
     *
     * @return ResponseEntity with "OK" message
     * @throws HttpStatus.OK (200) indicating the service is running
     */
    @Operation(summary = "Get related books for a specific ISBN", description = "Retrieves a list of related books based on the provided ISBN using an external recommendation engine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved related books", content = @Content(schema = @Schema(implementation = RelatedBook.class))),
            @ApiResponse(responseCode = "204", description = "No related books found"),
            @ApiResponse(responseCode = "503", description = "Circuit breaker is open"),
            @ApiResponse(responseCode = "504", description = "Recommendation service timed out"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{isbn}/related-books")
    public ResponseEntity<?> getRelatedBooks(@PathVariable String isbn) {
        try {
            ResponseEntity<?> response = recommendationService.getRelatedBooks(isbn);

            // Simply return the response from the service
            return response;
        } catch (HttpServerErrorException.GatewayTimeout e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        } catch (HttpServerErrorException.ServiceUnavailable e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}