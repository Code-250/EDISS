package bookservice.entity;

import bookservice.dto.BookDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    private String isbn;
    private String title;
    private String author;
    private String description;
    private String genre;
    private Double price;
    private Integer quantity;

    public Book() {}

    public Book(BookDTO bookDTO) {
        this.isbn = bookDTO.getIsbn();
        this.title = bookDTO.getTitle();
        this.author = bookDTO.getAuthor();
        this.description = bookDTO.getDescription();
        this.genre = bookDTO.getGenre();
        this.price = bookDTO.getPrice();
        this.quantity = bookDTO.getQuantity();
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