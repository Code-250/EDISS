package bookservice.controller;

import bookservice.dto.BookDTO;
import bookservice.entity.Book;
import bookservice.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

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

    @PutMapping("/{isbn}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String isbn, @Valid @RequestBody BookDTO bookDTO) {
        if (!bookRepository.existsById(isbn)) {
            return ResponseEntity.notFound().build();
        }
        Book book = new Book(bookDTO);
        book.setIsbn(isbn);
        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(new BookDTO(updatedBook));
    }

    @GetMapping({"/isbn/{isbn}", "/{isbn}"})
    public ResponseEntity<BookDTO> getBook(@PathVariable String isbn) {
        return bookRepository.findById(isbn)
                .map(book -> ResponseEntity.ok(new BookDTO(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain")
                .body("OK");
    }
}