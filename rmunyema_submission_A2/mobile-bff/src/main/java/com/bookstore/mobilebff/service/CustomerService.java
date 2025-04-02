package com.bookstore.mobilebff.service;

import com.bookstore.mobilebff.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
        ResponseEntity<CustomerDTO> response = restTemplate.getForEntity(baseUrl + "/customers/" + id, CustomerDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            CustomerDTO transformedCustomer = transformCustomerForMobile(response.getBody());
            return new ResponseEntity<>(transformedCustomer, HttpStatus.OK);
        }

        return response;
    }

    public ResponseEntity<CustomerDTO> getCustomerByUserId(String userId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/customers")
                .queryParam("userId", userId)
                .toUriString();

        ResponseEntity<CustomerDTO> response = restTemplate.getForEntity(url, CustomerDTO.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            CustomerDTO transformedCustomer = transformCustomerForMobile(response.getBody());
            return new ResponseEntity<>(transformedCustomer, HttpStatus.OK);
        }

        return response;
    }

    public ResponseEntity<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        return restTemplate.postForEntity(baseUrl + "/customers", customerDTO, CustomerDTO.class);
    }

    /**
     * Transform customer data for mobile clients: Remove address fields
     */
    private CustomerDTO transformCustomerForMobile(CustomerDTO customer) {
        if (customer != null) {
            // Create a new DTO with only the necessary fields
            CustomerDTO mobileCustomer = new CustomerDTO();
            mobileCustomer.setId(customer.getId());
            mobileCustomer.setUserId(customer.getUserId());
            mobileCustomer.setName(customer.getName());
            mobileCustomer.setPhone(customer.getPhone());
            mobileCustomer.setCity(customer.getCity());
            mobileCustomer.setState(customer.getState());
            mobileCustomer.setZipcode(customer.getZipcode());

            // Address fields are deliberately omitted:
            // - address
            // - address2
            // - city
            // - state
            // - zipcode

            return mobileCustomer;
        }
        return customer;
    }
}