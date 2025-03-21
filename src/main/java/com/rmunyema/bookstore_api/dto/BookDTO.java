package com.rmunyema.bookstore_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rmunyema.bookstore_api.entity.Book;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

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

    public BookDTO() {}

    public BookDTO(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.description = book.getDescription();
        this.genre = book.getGenre();
        this.price = book.getPrice();
        this.quantity = book.getQuantity();
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    // Setters
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}