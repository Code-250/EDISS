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

    // The @JsonProperty annotation is used to specify the name of the property
    // when serialized to JSON. In this case, the property name is "Title".
    @JsonProperty("Title")
    @NotNull
    private String title;

    // The @JsonProperty annotation is used to specify the name of the property
    // when serialized to JSON. In this case, the property name is "Author".
    // The @NotNull annotation is used to specify that this field cannot be null.
    @NotNull
    @JsonProperty("Author")
    private String author;

    // The @JsonProperty annotation is used to specify the name of the property
    // when serialized to JSON. In this case, the property name is "Description".
    // The @NotNull annotation is used to specify that this field cannot be null.
    @NotNull
    private String description;

    // The @JsonProperty annotation is used to specify the name of the property
    // when serialized to JSON. In this case, the property name is "Genre".
    // The @NotNull annotation is used to specify that this field cannot be null.
    @NotNull
    private String genre;

    // The @JsonProperty annotation is used to specify the name of the property
    // when serialized to JSON. In this case, the property name is "Publisher".
    // The @NotNull annotation is used to specify that this field cannot be null.
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    private Double price;

    @NotNull
    private Integer quantity;
}