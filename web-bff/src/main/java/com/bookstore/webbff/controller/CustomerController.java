package com.bookstore.webbff.controller;

import com.bookstore.webbff.dto.CustomerDTO;
import com.bookstore.webbff.service.CustomerService;
import com.bookstore.webbff.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@RequestHeader("Authorization") String authHeader,
                                       @RequestHeader(value = "X-Client-Type", required = false) String clientType,
                                       @PathVariable("id") String id) {
        validateHeaders(authHeader, clientType);
        return customerService.getCustomerById(id);
    }

    @GetMapping
    public CustomerDTO getCustomerByUserId(@RequestHeader("Authorization") String authHeader,
                                           @RequestHeader(value = "X-Client-Type", required = false) String clientType,
                                           @RequestParam("userId") String userId) {
        validateHeaders(authHeader, clientType);
        return customerService.getCustomerByUserId(userId);
    }

    private void validateHeaders(String authHeader, String clientType) {
        if (clientType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "X-Client-Type header is required");
        }
        JwtUtil.validateJwt(authHeader);
    }
}