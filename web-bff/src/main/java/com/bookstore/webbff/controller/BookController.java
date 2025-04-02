package com.bookstore.webbff.controller;

import com.bookstore.webbff.dto.BookDTO;
import com.bookstore.webbff.service.BookService;
import com.bookstore.webbff.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/isbn/{isbn}")
    public BookDTO getBookByIsbn(@RequestHeader("Authorization") String authHeader,
                                 @RequestHeader(value = "X-Client-Type", required = false) String clientType,
                                 @PathVariable("isbn") String isbn) {
        validateHeaders(authHeader, clientType);
        return bookService.getBookByIsbn(isbn);
    }

    private void validateHeaders(String authHeader, String clientType) {
        if (clientType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "X-Client-Type header is required");
        }
        JwtUtil.validateJwt(authHeader);
    }
}