package customerservice.dto;

import customerservice.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class KafkaResponseDTO {
/**
 * CustomerDTO is a Data Transfer Object representing customer details.
 * It is used to transfer customer data between the client and server.
 */

    private Long id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String name;

    /**
     * Default constructor for CustomerDTO.
     */
    public KafkaResponseDTO() {
    }

    /**
     * Constructor to create a CustomerDTO from a Customer entity.
     *
     * @param customer The Customer entity to convert to DTO.
     */
    public KafkaResponseDTO(Customer customer) {
        this.id = customer.getId();
        this.email = customer.getUserId();
        this.name = customer.getName();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}

