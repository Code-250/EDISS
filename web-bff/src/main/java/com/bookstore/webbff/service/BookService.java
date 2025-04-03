package com.bookstore.webbff.service;

import com.bookstore.webbff.dto.BookDTO;
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
        return restTemplate.getForEntity(baseUrl + "/books/" + isbn, BookDTO.class);
    }

    public ResponseEntity<BookDTO> createBook(BookDTO bookDTO) {
        return restTemplate.postForEntity(baseUrl + "/books", bookDTO, BookDTO.class);
    }

    public ResponseEntity<BookDTO> updateBook(String isbn, BookDTO bookDTO) {
        return restTemplate.exchange(
                baseUrl + "/books/" + isbn,
                HttpMethod.PUT,
                new HttpEntity<>(bookDTO),
                BookDTO.class);
    }
}