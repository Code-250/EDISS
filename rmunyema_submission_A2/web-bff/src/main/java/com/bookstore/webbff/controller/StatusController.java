package com.bookstore.webbff.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    /*
     * This endpoint checks the status of the application.
     * It returns a simple "OK" response with a 200 OK status.
     * The @GetMapping annotation is used to map HTTP GET requests to this method.
     * The @RequestMapping annotation is used to specify the base URL for this
     * controller.
     */
    @GetMapping
    public ResponseEntity<String> getStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<>("OK", headers, HttpStatus.OK);
    }
}