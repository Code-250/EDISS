package com.bookstore.webbff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerDTO is a Data Transfer Object that represents a customer in the
 * system.
 * It contains fields for the customer's ID, user ID, name, phone, address,
 * city,
 * state, and zipcode.
 */
@Data
@NoArgsConstructor
public class CustomerDTO {
        private Long id;

        @NotNull
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format. Must include a domain with TLD (e.g., .com, .org)")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format. Must include a domain with TLD (e.g., .com, .org)")
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
        @Pattern(regexp = "[A-Z]{2}")
        private String state;

        @NotNull
        private String zipcode;
}