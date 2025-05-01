package customerservice.controller;

import customerservice.dto.CustomerDTO;
import customerservice.entity.Customer;
import customerservice.repository.CustomerRepository;
import customerservice.service.KafkaProducerService;
import customerservice.util.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * CustomerController handles HTTP requests related to customer operations.
 * It provides endpoints for adding a new customer, retrieving customer details
 * by ID or user ID.
 */
@RestController
@RequestMapping("/customers")
@Tag(name = "Customer API", description = "Operations for managing customer data")
public class CustomerController {

        private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
        /**
         * The CustomerRepository instance for database operations.
         */
        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private KafkaProducerService kafkaProducerService;

        /**
         * Adds a new customer to the system.
         *
         * @param customerDTO The customer data transfer object containing customer
         *                    details.
         * @return ResponseEntity with the created customer details or an error message.
         */
        @Operation(summary = "Create a new customer", description = "Creates a new customer and publishes a registration event to Kafka")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(implementation = Customer.class))),
                        @ApiResponse(responseCode = "404", description = "Customer Already Exists"),
        }

        )
        @PostMapping
        public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
                if (customerRepository.findByUserId(customerDTO.getUserId()).isPresent())
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .body(Map.of("message", "This user ID already exists in the system."));

                Customer customer = new Customer(customerDTO);
                Map<String, Object> response = new HashMap<>();
                Customer savedCustomer = customerRepository.save(customer);
                response.put("id", savedCustomer.getId());
                response.put("userId", savedCustomer.getUserId());
                response.put("name", savedCustomer.getName());
                response.put("phone", savedCustomer.getPhone());
                response.put("address", savedCustomer.getAddress());
                response.put("city", savedCustomer.getCity());
                response.put("state", savedCustomer.getState());
                response.put("address2", savedCustomer.getAddress2());
                kafkaProducerService.sendCustomerEvent(savedCustomer);
                log.info("response {}", response);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .header("Location", "/customers/" + savedCustomer.getId())
                                .body(new CustomerDTO(savedCustomer));
        }

        /**
         * Retrieves customer details by ID.
         *
         * @param id The ID of the customer to retrieve.
         * @return ResponseEntity with the customer details or a not found status.
         */
        @Operation(summary = "Get customer by ID", description = "Retrieves a specific customer by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = Customer.class))),
                        @ApiResponse(responseCode = "404", description = "Customer not found")
        })
        @GetMapping("/{id}")
        public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
                return customerRepository.findById(id)
                                .map(customer -> ResponseEntity.ok(new CustomerDTO(customer)))
                                .orElse(ResponseEntity.notFound().build());
        }

        /**
         * Retrieves customer details by ISBN.
         *
         * @param id The ISBN of the customer to retrieve.
         * @return ResponseEntity with the customer details or a not found status.
         */
        @GetMapping("isbn/{id}")
        public ResponseEntity<CustomerDTO> getCustomerIsbnById(@PathVariable Long id) {
                return customerRepository.findById(id)
                                .map(customer -> ResponseEntity.ok(new CustomerDTO(customer)))
                                .orElse(ResponseEntity.notFound().build());
        }

        /**
         * Retrieves customer details by user ID.
         *
         * @param userId The user ID of the customer to retrieve.
         * @return ResponseEntity with the customer details or a not found status.
         */
        @GetMapping
        public ResponseEntity<CustomerDTO> getCustomerByUserId(@RequestParam String userId) {
                if (!ValidationService.isValidEmail(userId)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(null);
                }
                return customerRepository.findByUserId(userId)
                                .map(customer -> ResponseEntity.ok(new CustomerDTO(customer)))
                                .orElse(ResponseEntity.notFound().build());
        }
}