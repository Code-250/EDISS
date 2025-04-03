package com.bookstore.webbff.controller;

import com.bookstore.webbff.dto.BookDTO;
import com.bookstore.webbff.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /*
     * This endpoint retrieves a book by its ISBN.
     * It supports two URL patterns: /books/isbn/{isbn} and /books/{isbn}.
     * The first pattern is more explicit, while the second one is shorter.
     * The method returns a ResponseEntity containing the BookDTO object if found,
     * or a 404 Not Found status if the book is not found.
     * The method also handles exceptions for better error handling.
     * The @GetMapping annotation is used to map HTTP GET requests to this method.
     * The @PathVariable annotation is used to extract the ISBN from the URL.
     * The @ResponseStatus annotation is used to specify the HTTP status code for
     * the response.
     * The @Valid annotation is used to validate the BookDTO object.
     * The @RequestBody annotation is used to bind the request body to the BookDTO
     * object.
     * The @Autowired annotation is used to inject the BookService dependency.
     * The @RestController annotation is used to indicate that this class is a REST
     * controller.
     * The @RequestMapping annotation is used to specify the base URL for this
     * controller.
     */
    @GetMapping({ "/isbn/{isbn}", "/{isbn}" })
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        try {
            ResponseEntity<BookDTO> foundBook = bookService.getBook(isbn);
            return ResponseEntity.status(HttpStatus.OK).body(foundBook.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*
     * This endpoint retrieves a list of all books.
     * It returns a ResponseEntity containing a list of BookDTO objects.
     * The method also handles exceptions for better error handling.
     * The @GetMapping annotation is used to map HTTP GET requests to this method.
     * The @ResponseStatus annotation is used to specify the HTTP status code for
     * the response.
     * The @Valid annotation is used to validate the BookDTO object.
     * The @RequestBody annotation is used to bind the request body to the BookDTO
     * object.
     * The @Autowired annotation is used to inject the BookService dependency.
     * The @RestController annotation is used to indicate that this class is a REST
     * controller.
     * The @RequestMapping annotation is used to specify the base URL for this
     * controller.
     */
    @PostMapping
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        try {
            ResponseEntity<?> createdBook = bookService.createBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("message", "This ISBN already exists in the system."));
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /*
     * This endpoint updates a book by its ISBN.
     * It returns a ResponseEntity containing the updated BookDTO object.
     * The method also handles exceptions for better error handling.
     * The @PutMapping annotation is used to map HTTP PUT requests to this method.
     * The @ResponseStatus annotation is used to specify the HTTP status code for
     * the response.
     * The @Valid annotation is used to validate the BookDTO object.
     * The @RequestBody annotation is used to bind the request body to the BookDTO
     * object.
     * The @Autowired annotation is used to inject the BookService dependency.
     * The @RestController annotation is used to indicate that this class is a REST
     * controller.
     * The @RequestMapping annotation is used to specify the base URL for this
     * controller.
     */
    @PutMapping("/{isbn}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @Valid @RequestBody BookDTO bookDTO) {
        try {
            ResponseEntity<BookDTO> updatedBook = bookService.updateBook(isbn, bookDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedBook.getBody());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}