package com.bookstore.webbff.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * StatusController is a REST controller that handles HTTP requests related to
 * system status.
 * It provides an endpoint to check the health status of the application.
 */
@RestController
@RequestMapping("/status")
@Tag(name = "Web BFF API Status", description = "Backend for Frontend API for web clients Service Up")
public class StatusController {

    /**
     * Retrieves the health status of the application.
     *
     * @return A ResponseEntity containing the status message and HTTP headers.
     */
    @Operation(
            summary = "Web BFF APi Status Check",
            description = "Get Customer by Customer Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Web Bff Service successfully is up and running")
    })
    @GetMapping
    public ResponseEntity<String> getStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<>("OK", headers, HttpStatus.OK);
    }
}