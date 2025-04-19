package com.bookstore.mobilebff.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BookDTO.java
 * This class is a Data Transfer Object (DTO) that represents a book.
 * It contains fields for the book's ISBN, title, author, description, genre,
 * price, and quantity.
 * The class uses Lombok annotations for automatic generation of getters,
 * setters, and constructors.
 */
@Data
@NoArgsConstructor
public class BookDTO {
    @NotNull
    @JsonProperty("ISBN") // JSON property name for the ISBN field
    private String isbn;

    @NotNull
    private String title;

    @NotNull
    @JsonProperty("Author") // JSON property name for the author field
    private String author;

    @NotNull
    private String description;

    @NotNull
    private String genre;

    @NotNull
    @DecimalMin("0.00") // Minimum value for price
    @Digits(integer = 10, fraction = 2) // Maximum Decimal digits for price
    private Double price;

    @NotNull
    private Integer quantity;
}