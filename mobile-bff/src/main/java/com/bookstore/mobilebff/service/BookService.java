package com.bookstore.mobilebff.service;

import com.bookstore.mobilebff.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public BookService(RestTemplate restTemplate,
                       @Value("${backend.services.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ResponseEntity<BookDTO> getBook(String isbn) {
        ResponseEntity<BookDTO> response = restTemplate.getForEntity(baseUrl + "/books/" + isbn, BookDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            transformBookForMobile(response.getBody());
        }

        return response;
    }

    public ResponseEntity<BookDTO> createBook(BookDTO bookDTO) {
        return restTemplate.postForEntity(baseUrl + "/books", bookDTO, BookDTO.class);
    }

    public ResponseEntity<BookDTO> updateBook(String isbn, BookDTO bookDTO) {
        ResponseEntity<BookDTO> response = restTemplate.exchange(
                baseUrl + "/books/" + isbn,
                HttpMethod.PUT,
                new HttpEntity<>(bookDTO),
                BookDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            transformBookForMobile(response.getBody());
        }

        return response;
    }

    /**
     * Transform book genre for mobile clients: Replace "non-fiction" with "3"
     */
    private void transformBookForMobile(BookDTO book) {
        if (book != null && "non-fiction".equalsIgnoreCase(book.getGenre())) {
            book.setGenre("3");
        }
    }
}