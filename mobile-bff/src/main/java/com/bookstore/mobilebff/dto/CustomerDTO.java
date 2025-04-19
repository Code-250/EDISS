package com.bookstore.mobilebff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerDTO.java
 * This class is a Data Transfer Object (DTO) that represents a customer.
 * It contains fields for the customer's ID, user ID, name, phone, address,
 * address2, city, state, and zipcode.
 * The class uses Lombok annotations for automatic generation of getters,
 * setters, and constructors.
 */
@Data
@NoArgsConstructor
public class CustomerDTO {
        private Long id;

        @NotNull
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format. Must include a domain with TLD (e.g., .com, .org)")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format. Must include a domain with TLD (e.g., .com, .org)") // Email
        private String userId;

        @NotNull
        private String name;

        @NotNull
        private String phone;

        @NotNull
        private String address;

        private String address2;

        @NotNull
        private String city;

        @NotNull
        @Pattern(regexp = "[A-Z]{2}") // State format validation (2 uppercase letters)
        private String state;

        @NotNull
        private String zipcode;
}