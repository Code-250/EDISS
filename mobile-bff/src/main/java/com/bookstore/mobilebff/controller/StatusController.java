package com.bookstore.mobilebff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * StatusController.java
 * This class is a Spring Boot REST controller that handles HTTP requests related to the status of the application.
 * It provides an endpoint to check the status of the application.
 * The controller returns a simple "OK" response with a 200 OK HTTP status code.
 */
@RestController
@RequestMapping("/status")
@Tag(name = "Mobile BFF API", description = "Backend for Frontend API for mobile clients")
public class StatusController {

    /*
     * getStatus() method handles GET requests to check the status of the
     * application.
     * It returns a ResponseEntity containing a simple "OK" message and an HTTP
     * status code.
     * 
     * @return ResponseEntity containing the status message and HTTP status code.
     */
    @Operation(
            summary = "Get service status",
            description = "Check if the Mobile BFF service is up and running"
    )
    @ApiResponse(responseCode = "200", description = "Service is up")
    @GetMapping
    public ResponseEntity<String> getStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<>("OK", headers, HttpStatus.OK);
    }
}