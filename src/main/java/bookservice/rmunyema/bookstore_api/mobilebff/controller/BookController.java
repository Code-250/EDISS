package bookservice.rmunyema.bookstore_api.mobilebff.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/books")
public class BookController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.services.base-url}")
    private String backendBaseUrl;

    @GetMapping
    public ResponseEntity<String> getAllBooks() {
        ResponseEntity<String> response = restTemplate.getForEntity(backendBaseUrl + "/books", String.class);
        return transformResponse(response);
    }

    @GetMapping({"/isbn/{isbn}", "/{isbn}"})
    public ResponseEntity<String> getBookByIsbn(@PathVariable String isbn) {
        ResponseEntity<String> response = restTemplate.getForEntity(backendBaseUrl + "/books/" + isbn, String.class);
        return transformResponse(response);
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody String bookData,
                                          @RequestHeader HttpHeaders headers) {
        HttpEntity<String> request = new HttpEntity<>(bookData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(backendBaseUrl + "/books", request, String.class);
        return transformResponse(response);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<String> updateBook(@PathVariable String isbn,
                                             @RequestBody String bookData,
                                             @RequestHeader HttpHeaders headers) {
        HttpEntity<String> request = new HttpEntity<>(bookData, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                backendBaseUrl + "/books/" + isbn,
                HttpMethod.PUT,
                request,
                String.class);
        return transformResponse(response);
    }

    private ResponseEntity<String> transformResponse(ResponseEntity<String> response) {
        try {
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String body = response.getBody();
                // Replace "non-fiction" with "3" in the response
                body = body.replace("\"non-fiction\"", "\"3\"");

                return ResponseEntity.status(response.getStatusCode())
                        .headers(response.getHeaders())
                        .body(body);
            }
        } catch (Exception e) {
            // If there's an error in transformation, return the original response
        }
        return response;
    }
}