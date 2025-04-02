package com.bookstore.webbff.service;

import com.bookstore.webbff.dto.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", url = "${backend.base.url}")
public interface BookService {
    @GetMapping("/books/isbn/{isbn}")
    BookDTO getBookByIsbn(@PathVariable("isbn") String isbn);
}