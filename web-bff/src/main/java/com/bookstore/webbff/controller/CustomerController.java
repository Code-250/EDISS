package com.bookstore.webbff.controller;

import com.bookstore.webbff.dto.CustomerDTO;
import com.bookstore.webbff.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

/**
 * CustomerController is a REST controller that handles HTTP requests related to
 * customers.
 * It provides endpoints to get and add customer information.
 */
@RestController
@RequestMapping("/customers")
@Slf4j
@Tag(name = "Web BFF API Customer", description = "Backend for Frontend API for web clients")
public class CustomerController {

    /**
     * The CustomerService instance used to handle customer-related operations.
     */
    @Autowired
    private CustomerService customerService;

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return A ResponseEntity containing the CustomerDTO if found, or an error
     *         response if not found.
     */
    @Operation(summary = "Get a customer", description = "Get Customer by Customer Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer Not Found"),
            @ApiResponse(responseCode = "400", description = "Something Went wrong while retrieving Customer"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        try {
            ResponseEntity<CustomerDTO> foundCustomer = customerService.getCustomerById(id);
            return ResponseEntity.status(HttpStatus.OK).body(foundCustomer.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Retrieves a customer by their user ID.
     *
     * @param userId The user ID of the customer to retrieve.
     * @return A ResponseEntity containing the CustomerDTO if found, or an error
     *         response if not found.
     */
    @Operation(summary = "Get a customer", description = "Get Customer by Customer user Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer Not Found"),
            @ApiResponse(responseCode = "400", description = "Something Went wrong while retrieving Customer"),
    })
    @GetMapping
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@RequestParam String userId) {
        try {
            ResponseEntity<CustomerDTO> foundCustomer = customerService.getCustomerByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(foundCustomer.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Adds a new customer to the system.
     *
     * @param customerDTO The CustomerDTO containing the details of the customer to
     *                    add.
     * @return A ResponseEntity containing the created CustomerDTO or an error
     *         response if the customer already exists.
     */
    @Operation(summary = "Create a new customer", description = "Registers a new customer and triggers a welcome email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "422", description = "Customer Already Exists"),
            @ApiResponse(responseCode = "400", description = "Something Went wrong while creating Customer"),
    })
    @PostMapping
    public ResponseEntity<CustomerDTO> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        try {
            ResponseEntity<CustomerDTO> createdCustomer = customerService.createCustomer(customerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer.getBody());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (HttpClientErrorException.BadRequest e) {
            log.info("Bad request " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}