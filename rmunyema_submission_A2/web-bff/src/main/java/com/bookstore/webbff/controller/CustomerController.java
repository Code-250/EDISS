package com.bookstore.webbff.controller;

import com.bookstore.webbff.dto.CustomerDTO;
import com.bookstore.webbff.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*
     * This endpoint retrieves a customer by its ID.
     * It returns a ResponseEntity containing the CustomerDTO object if found,
     * or a 404 Not Found status if the customer is not found.
     * The method also handles exceptions for better error handling.
     * The @GetMapping annotation is used to map HTTP GET requests to this method.
     * The @PathVariable annotation is used to extract the ID from the URL.
     * The @ResponseStatus annotation is used to specify the HTTP status code for
     * the response.
     * The @Valid annotation is used to validate the CustomerDTO object.
     * The @RequestBody annotation is used to bind the request body to the
     * CustomerDTO
     * object.
     * The @Autowired annotation is used to inject the CustomerService dependency.
     * The @RestController annotation is used to indicate that this class is a REST
     * controller.
     * The @RequestMapping annotation is used to specify the base URL for this
     * controller.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        try {
            ResponseEntity<CustomerDTO> foundCustomer = customerService.getCustomerById(id);
            return ResponseEntity.status(HttpStatus.OK).body(foundCustomer.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*
     * This endpoint retrieves a customer by its user ID.
     * It returns a ResponseEntity containing the CustomerDTO object if found,
     * or a 404 Not Found status if the customer is not found.
     * The method also handles exceptions for better error handling.
     * The @GetMapping annotation is used to map HTTP GET requests to this method.
     * The @RequestParam annotation is used to extract the user ID from the URL.
     * The @ResponseStatus annotation is used to specify the HTTP status code for
     * the response.
     * The @Valid annotation is used to validate the CustomerDTO object.
     * The @RequestBody annotation is used to bind the request body to the
     * CustomerDTO
     * object.
     * The @Autowired annotation is used to inject the CustomerService dependency.
     * The @RestController annotation is used to indicate that this class is a REST
     * controller.
     * The @RequestMapping annotation is used to specify the base URL for this
     * controller.
     */
    @GetMapping
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@RequestParam String userId) {
        try {
            ResponseEntity<CustomerDTO> foundCustomer = customerService.getCustomerByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(foundCustomer.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*
     * This endpoint creates a new customer.
     * It returns a ResponseEntity containing the created CustomerDTO object.
     * The method also handles exceptions for better error handling.
     * The @PostMapping annotation is used to map HTTP POST requests to this method.
     * The @RequestBody annotation is used to bind the request body to the
     * CustomerDTO
     * object.
     * The @ResponseStatus annotation is used to specify the HTTP status code for
     * the response.
     * The @Valid annotation is used to validate the CustomerDTO object.
     * The @Autowired annotation is used to inject the CustomerService dependency.
     * The @RestController annotation is used to indicate that this class is a REST
     * controller.
     * The @RequestMapping annotation is used to specify the base URL for this
     * controller.
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        try {
            ResponseEntity<CustomerDTO> createdCustomer = customerService.createCustomer(customerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer.getBody());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}