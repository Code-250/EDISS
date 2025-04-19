package com.bookstore.webbff.service;

import com.bookstore.webbff.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * CustomerService is a service class that handles operations related to
 * customers.
 * It interacts with the backend service to perform CRUD operations on customer
 * data.
 */
@Service
public class CustomerService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    /**
     * Constructor for CustomerService.
     *
     * @param restTemplate The RestTemplate instance used for making HTTP requests.
     * @param baseUrl      The base URL of the backend service.
     */
    @Autowired
    public CustomerService(RestTemplate restTemplate,
            @Value("http://customer-service:3000") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return A ResponseEntity containing the CustomerDTO if found, or an error
     *         response if not found.
     */
    public ResponseEntity<CustomerDTO> getCustomerById(Long id) {
        try {
            return restTemplate.getForEntity(baseUrl + "/customers/" + id, CustomerDTO.class);
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting customer", e);
        }

    }

    /**
     * Retrieves a customer by their user ID.
     *
     * @param userId The user ID of the customer to retrieve.
     * @return A ResponseEntity containing the CustomerDTO if found, or an error
     *         response if not found.
     */
    public ResponseEntity<CustomerDTO> getCustomerByUserId(String userId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/customers")
                    .queryParam("userId", userId)
                    .toUriString();
            return restTemplate.getForEntity(url, CustomerDTO.class);
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting customer", e);
        }

    }

    /**
     * Creates a new customer.
     *
     * @param customerDTO The CustomerDTO containing the details of the customer to
     *                    create.
     * @return A ResponseEntity containing the created CustomerDTO or an error
     *         response if the customer already exists.
     */
    public ResponseEntity<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        try {
            return restTemplate.postForEntity(baseUrl + "/customers", customerDTO, CustomerDTO.class);
        } catch (HttpClientErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating customer", e);
        }

    }
}