package bookservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing the status of the service.
 * Provides an endpoint to check the health status of the service.
 */
@RestController
@RequestMapping("/status")
public class StatusController {

    /**
     * Endpoint to check the health status of the service.
     *
     * @return ResponseEntity with a plain text "OK" message and HTTP status 200 OK
     */
    @GetMapping
    public ResponseEntity<String> getStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<>("OK", headers, HttpStatus.OK);
    }
}