package com.bookstore.webbff.service;

import com.bookstore.webbff.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CustomerService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public CustomerService(RestTemplate restTemplate,
                           @Value("${backend.services.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ResponseEntity<CustomerDTO> getCustomerById(Long id) {
        return restTemplate.getForEntity(baseUrl + "/customers/" + id, CustomerDTO.class);
    }

    public ResponseEntity<CustomerDTO> getCustomerByUserId(String userId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/customers")
                .queryParam("userId", userId)
                .toUriString();
        return restTemplate.getForEntity(url, CustomerDTO.class);
    }

    public ResponseEntity<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        return restTemplate.postForEntity(baseUrl + "/customers", customerDTO, CustomerDTO.class);
    }
}