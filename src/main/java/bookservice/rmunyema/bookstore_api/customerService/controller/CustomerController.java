package bookservice.rmunyema.bookstore_api.customerService.controller;

import bookservice.rmunyema.bookstore_api.customerService.dto.CustomerDTO;
import bookservice.rmunyema.bookstore_api.customerService.entity.Customer;
import bookservice.rmunyema.bookstore_api.customerService.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        if (customerRepository.findByUserId(customerDTO.getUserId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("message", "This user ID already exists in the system."));
        }
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

    @GetMapping
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@RequestParam String userId) {
        // Validate email format
        if (userId == null || !EMAIL_PATTERN.matcher(userId).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return customerRepository.findByUserId(userId)
                .map(customer -> ResponseEntity.ok(new CustomerDTO(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain")
                .body("OK");
    }
}