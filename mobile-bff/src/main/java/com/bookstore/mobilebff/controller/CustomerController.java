package com.bookstore.mobilebff.controller;

import com.bookstore.mobilebff.dto.CustomerDTO;
import com.bookstore.mobilebff.service.CustomerService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
 * CustomerController.java
 * This class is a Spring Boot REST controller that handles HTTP requests related to customers.
 * It provides endpoints to get and add customer information.
 * The controller interacts with the CustomerService to perform operations on customer data.
 * It uses Lombok for logging and validation annotations for request body validation.
 * The controller also handles exceptions and returns appropriate HTTP responses.
 */
@RestController
@RequestMapping("/customers")
@Slf4j
@Tag(name = " Book Mobile BFF API", description = "Backend for Frontend API for mobile clients")
public class CustomerController {

    /*
     * CustomerService is a service class that provides methods to interact with
     * customer data.
     * It is injected into the controller using Spring's @Autowired annotation.
     */
    @Autowired
    private CustomerService customerService;

    /*
     * getCustomerById() method handles GET requests to retrieve a customer by ID.
     * It returns a ResponseEntity containing the customer information and an HTTP
     * status code.
     * 
     * @Param id The ID of the customer to retrieve.
     * 
     * @return ResponseEntity containing the customer information and HTTP status
     * code.
     * 
     * @throws HttpClientErrorException.NotFound if the customer is not found.
     * 
     * @throws HttpClientErrorException.Unauthorized if the request is unauthorized.
     */
    @Operation(summary = "Get customer by ID", description = "Retrieves a specific customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            ResponseEntity<CustomerDTO> foundCustomer = customerService.getCustomerById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("id", Objects.requireNonNull(foundCustomer.getBody()).getId());
            response.put("userId", foundCustomer.getBody().getUserId());
            response.put("name", foundCustomer.getBody().getName());
            response.put("phone", foundCustomer.getBody().getPhone());
            response.put("address", foundCustomer.getBody().getAddress());
            response.put("address2", foundCustomer.getBody().getAddress2());
            response.put("city", foundCustomer.getBody().getCity());
            response.put("state", foundCustomer.getBody().getState());
            response.put("zipcode", foundCustomer.getBody().getZipcode());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*
     * getCustomerByUserId() method handles GET requests to retrieve a customer by
     * user ID.
     * It returns a ResponseEntity containing the customer information and an HTTP
     * status code.
     * 
     * @Param userId The user ID of the customer to retrieve.
     * 
     * @return ResponseEntity containing the customer information and HTTP status
     * code.
     * 
     * @throws HttpClientErrorException.NotFound if the customer is not found.
     * 
     * @throws HttpClientErrorException.BadRequest if the request is bad.
     */
    @Operation(summary = "Get customer by User  ID", description = "Retrieves a specific customer by their user  ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping
    public ResponseEntity<?> getCustomerByUserId(@RequestParam String userId) {
        try {
            ResponseEntity<CustomerDTO> foundCustomer = customerService.getCustomerByUserId(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("id", Objects.requireNonNull(foundCustomer.getBody()).getId());
            response.put("userId", foundCustomer.getBody().getUserId());
            response.put("name", foundCustomer.getBody().getName());
            response.put("phone", foundCustomer.getBody().getPhone());
            response.put("address", foundCustomer.getBody().getAddress());
            response.put("address2", foundCustomer.getBody().getAddress2());
            response.put("city", foundCustomer.getBody().getCity());
            response.put("state", foundCustomer.getBody().getState());
            response.put("zipcode", foundCustomer.getBody().getZipcode());
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /*
     * addCustomer() method handles POST requests to add a new customer.
     * It takes a CustomerDTO object as input and returns a ResponseEntity with the
     * created customer information.
     * 
     * @param customerDTO The CustomerDTO object containing customer details.
     * 
     * @return ResponseEntity containing the created customer information and HTTP
     * status code.
     * 
     * @throws HttpClientErrorException.UnprocessableEntity if the request is
     * unprocessable.
     * 
     * @throws HttpClientErrorException.BadRequest if the request is bad.
     * 
     * @throws HttpClientErrorException.Unauthorized if the request is unauthorized.
     */
    @Operation(summary = "Create a new customer", description = "Registers a new customer and triggers a welcome email")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "422", description = "Customer Already Exists"),
            @ApiResponse(responseCode = "400", description = "Something went wrong while creating a customer")
    })
    @PostMapping
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        try {
            ResponseEntity<CustomerDTO> createdCustomer = customerService.createCustomer(customerDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("id", Objects.requireNonNull(createdCustomer.getBody()).getId());
            response.put("userId", createdCustomer.getBody().getUserId());
            response.put("name", createdCustomer.getBody().getName());
            response.put("phone", createdCustomer.getBody().getPhone());
            response.put("address", createdCustomer.getBody().getAddress());
            response.put("address2", createdCustomer.getBody().getAddress2());
            response.put("city", createdCustomer.getBody().getCity());
            response.put("state", createdCustomer.getBody().getState());
            response.put("zipcode", createdCustomer.getBody().getZipcode());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        } catch (HttpClientErrorException.BadRequest e) {
            log.info("Bad request " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}