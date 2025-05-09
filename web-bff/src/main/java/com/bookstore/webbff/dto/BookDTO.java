package com.bookstore.webbff.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {
    @NotNull
    @JsonProperty("ISBN")
    private String isbn;

    @NotNull
    private String title;

    @NotNull
    @JsonProperty("Author")
    private String author;

    @NotNull
    private String description;

    @NotNull
    private String genre;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private Double price;

    @NotNull
    private Integer quantity;
}