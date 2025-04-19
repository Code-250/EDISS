package com.bookstore.mobilebff.service;

import com.bookstore.mobilebff.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * CustomerService.java
 * This class is a Spring Boot service that handles operations related to
 * customers.
 * It interacts with a backend service to perform CRUD operations on customer
 * data.
 * The service uses RestTemplate to make HTTP requests to the backend service.
 */
@Service
public class CustomerService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    /**
     * Constructor for CustomerService.
     * It initializes the RestTemplate and base URL for the backend service.
     *
     * @param restTemplate The RestTemplate to make HTTP requests.
     * @param baseUrl      The base URL of the backend service.
     */
    @Autowired
    public CustomerService(RestTemplate restTemplate,
            @Value("http://customer-service:3000") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * Get a customer by their ID.
     * It makes a GET request to the backend service to retrieve the customer
     * information.
     *
     * @param id The ID of the customer to retrieve.
     * @return ResponseEntity containing the customer information and HTTP status
     *         code.
     */
    public ResponseEntity<CustomerDTO> getCustomerById(Long id) {
       return restTemplate.getForEntity(baseUrl + "/customers/" + id,
                CustomerDTO.class);

    }

    /**
     * Get a customer by their user ID.
     * It makes a GET request to the backend service to retrieve the customer
     * information.
     *
     * @param userId The user ID of the customer to retrieve.
     * @return ResponseEntity containing the customer information and HTTP status
     *         code.
     */
    public ResponseEntity<CustomerDTO> getCustomerByUserId(String userId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/customers")
                .queryParam("userId", userId)
                .toUriString();

        return restTemplate.getForEntity(url, CustomerDTO.class);

//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            CustomerDTO transformedCustomer = transformCustomerForMobile(response.getBody());
//            return new ResponseEntity<>(transformedCustomer, HttpStatus.OK);
//        }
//
//        return response;
    }

    /**
     * Create a new customer.
     * It makes a POST request to the backend service to create a new customer.
     *
     * @param customerDTO The customer information to create.
     * @return ResponseEntity containing the created customer information and HTTP
     *         status code.
     */
    public ResponseEntity<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        return restTemplate.postForEntity(baseUrl + "/customers", customerDTO, CustomerDTO.class);
    }

    /**
     * Update an existing customer.
     * It makes a PUT request to the backend service to update the customer
     * information.
     *
     * @param id          The ID of the customer to update.
     * @param customerDTO The updated customer information.
     * @return ResponseEntity containing the updated customer information and HTTP
     *         status code.
     */
    private CustomerDTO transformCustomerForMobile(CustomerDTO customer) {
        if (customer != null) {
            // Create a new DTO with only the necessary fields
            CustomerDTO mobileCustomer = new CustomerDTO();
            mobileCustomer.setId(customer.getId());
            mobileCustomer.setUserId(customer.getUserId());
            mobileCustomer.setName(customer.getName());
            mobileCustomer.setPhone(customer.getPhone());
            mobileCustomer.setAddress(customer.getAddress());
            mobileCustomer.setCity(customer.getCity());
            mobileCustomer.setState(customer.getState());
            mobileCustomer.setZipcode(customer.getZipcode());

            return mobileCustomer;
        }
        return customer;
    }
}