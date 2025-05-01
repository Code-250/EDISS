package bookservice.controller;

import bookservice.dto.BookDTO;
import bookservice.entity.Book;
import bookservice.entity.RelatedBook;
import bookservice.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.Map;

/**
 * REST Controller for managing book resources.
 * Provides endpoints for creating, updating, and retrieving book information.
 */
@RestController
@RequestMapping("/cmd/books")
@Tag(name = "Book API", description = "Operations for managing books and retrieving recommendations")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookRepository bookRepository;
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

}