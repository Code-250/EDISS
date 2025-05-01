package bookqueryservice.controller;

import bookqueryservice.dto.BookDTO;
import bookqueryservice.dto.RecommendationResponseDto;
import bookqueryservice.entity.Book;
import bookqueryservice.entity.RelatedBook;
import bookqueryservice.repository.BookRepository;
import bookqueryservice.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

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
    @Autowired
    private MongoTemplate mongoTemplate;
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
        String _id = isbn.trim();
        logger.info("Retrieving book with ISBN {}", _id);
        logger.info("Received request to get book with ISBN: {}", isbn);

        // Log before the repository call
        logger.info("Attempting to find book in repository with ISBN: {}", isbn);

        // Store the result in a variable so we can log it
        Optional<Book> bookOptional = bookRepository.findById(isbn);

        // Log the result of the repository call
        if (bookOptional.isPresent()) {
            logger.info("Book found with ISBN: {}", isbn);
            Book book = bookOptional.get();
            logger.info("Book details: title={}, author={}", book.getTitle(), book.getAuthor());
            return ResponseEntity.ok(new BookDTO(book));
        } else {
            logger.warn("Book not found with ISBN: {}", isbn);
            return ResponseEntity.notFound().build();
        }
//        return bookRepository.findById(_id)
//                .map(book -> ResponseEntity.ok(new BookDTO(book)))
//                .orElse(ResponseEntity.notFound().build());
    }

//    get all
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.info("Received request to get all books");

        try {
            // Attempt to find all books
            logger.info("Querying repository for all books");
            List<Book> books = mongoTemplate.findAll(Book.class, "books_rmunyema");

            // Log the result
            logger.info("Found {} books in the repository", books.size());

            if (books.isEmpty()) {
                logger.info("No books found in the repository");
                return ResponseEntity.noContent().build();
            }

            // Convert to DTOs


//            logger.info("Successfully converted {} books to DTOs", Book.size());
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            logger.error("Error retrieving all books", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        logger.info("Getting related books for ISBN: {}", isbn);

        try {
            List<RecommendationResponseDto> recommendations = recommendationService.getRelatedBooks(isbn).join();

            // Check if the list is empty and return appropriate response
            if (recommendations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(recommendations);

        } catch (CompletionException e) {
            // Unwrap the CompletionException to get the actual cause
            Throwable cause = e.getCause();

            if (cause instanceof TimeoutException) {
                logger.error("Recommendation service timed out", cause);
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
            } else if (cause instanceof RecommendationService.CircuitOpenException) {
                logger.warn("Circuit is open, returning 503", cause);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            } else {
                logger.error("Error getting related books", cause);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            logger.error("Unexpected error getting related books", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Checks search books by a Keyword.
     *
     * @return ResponseEntity with "OK" message
     * @throws HttpStatus.OK (200) indicating the service is running
     */
    @Operation(summary = "Get booksaccording to the keyword searched", description = "Retrieves a list of books where a keyword is avalable in the title, genre, description, and author attribute.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved books according to the keyword passed", content = @Content(schema = @Schema(implementation = RelatedBook.class))),
            @ApiResponse(responseCode = "204", description = "No books found"),
            @ApiResponse(responseCode = "400", description = "status code if the keyword is not a String of a..z and A..Z characters only.")
    })
    @GetMapping("/")
    public ResponseEntity<?> searchBooks( @RequestParam(value = "keyword") String keyword) {
        if(!keyword.matches("^[a-zA-Z]+$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(keyword);
        TextQuery query = TextQuery.queryText(criteria);

        List<Book> books = mongoTemplate.find(query, Book.class, "books_rmunyema");

        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        logger.info("Found {} books matching keyword: {}", books.size(), keyword);
        return ResponseEntity.ok(books);
    }
}