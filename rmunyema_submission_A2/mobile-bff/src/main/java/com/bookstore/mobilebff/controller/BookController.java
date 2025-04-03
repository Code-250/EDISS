package com.bookstore.mobilebff.controller;

import com.bookstore.mobilebff.dto.BookDTO;
import com.bookstore.mobilebff.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping({"/isbn/{isbn}", "/{isbn}"})
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        try{
            ResponseEntity<BookDTO> foundBook = bookService.getBook(isbn);
            return ResponseEntity.status(HttpStatus.OK).body(foundBook.getBody());

        }catch (HttpClientErrorException.NotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        try {
            ResponseEntity<?> createdBook = bookService.createBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook.getBody());
        }catch (HttpClientErrorException.UnprocessableEntity e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }catch (HttpClientErrorException.BadRequest e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @Valid @RequestBody BookDTO bookDTO) {
        try {
            ResponseEntity<BookDTO> updatedBook = bookService.updateBook(isbn, bookDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedBook.getBody());
        }catch (HttpClientErrorException.NotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (HttpClientErrorException.BadRequest e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}