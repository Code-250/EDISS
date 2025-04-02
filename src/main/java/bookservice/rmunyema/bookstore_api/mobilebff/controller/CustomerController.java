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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.services.base-url}")
    private String backendBaseUrl;

    @GetMapping("/{id}")
    public ResponseEntity<String> getCustomerById(@PathVariable Long id) {
        ResponseEntity<String> response = restTemplate.getForEntity(backendBaseUrl + "/customers/" + id, String.class);
        return transformResponse(response);
    }

    @GetMapping
    public ResponseEntity<String> getCustomerByUserId(@RequestParam String userId) {
        String url = UriComponentsBuilder.fromHttpUrl(backendBaseUrl + "/customers")
                .queryParam("userId", userId)
                .toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return transformResponse(response);
    }

    @PostMapping
    public ResponseEntity<String> addCustomer(@RequestBody String customerData,
                                              @RequestHeader HttpHeaders headers) {
        HttpEntity<String> request = new HttpEntity<>(customerData, headers);
        return restTemplate.postForEntity(backendBaseUrl + "/customers", request, String.class);
    }

    private ResponseEntity<String> transformResponse(ResponseEntity<String> response) {
        try {
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                // For mobile clients, remove specific address fields
                if (rootNode.isObject()) {
                    removeAddressFields((ObjectNode) rootNode);
                } else if (rootNode.isArray()) {
                    for (JsonNode node : rootNode) {
                        if (node.isObject()) {
                            removeAddressFields((ObjectNode) node);
                        }
                    }
                }

                return ResponseEntity.status(response.getStatusCode())
                        .headers(response.getHeaders())
                        .body(objectMapper.writeValueAsString(rootNode));
            }
        } catch (IOException e) {
            // If there's an error in transformation, return the original response
        }
        return response;
    }

    private void removeAddressFields(ObjectNode node) {
        node.remove("address");
        node.remove("address2");
        node.remove("city");
        node.remove("state");
        node.remove("zipcode");
    }
}