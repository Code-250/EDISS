package com.rmunyema.bookstore_api.controller;

import com.rmunyema.bookstore_api.dto.CustomerDTO;
import com.rmunyema.bookstore_api.entity.Customer;
import com.rmunyema.bookstore_api.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        if (customerRepository.findByUserId(customerDTO.getUserId()).isPresent())
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("message", "This user ID already exists in the system."));
        Customer customer = new Customer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/customers/" + savedCustomer.getId())
                .body(new CustomerDTO(savedCustomer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(customer -> ResponseEntity.ok(new CustomerDTO(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("isbn/{id}")
    public ResponseEntity<CustomerDTO> getCustomerIsbnById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(customer -> ResponseEntity.ok(new CustomerDTO(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@RequestParam String userId) {
        return customerRepository.findByUserId(userId)
                .map(customer -> ResponseEntity.ok(new CustomerDTO(customer)))
                .orElse(ResponseEntity.notFound().build());
    }
}