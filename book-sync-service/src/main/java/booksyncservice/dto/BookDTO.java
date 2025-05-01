package booksyncservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private String isbn;
    private String title;
    private String author;
    private String description;
    private String genre;
    private Double price;
    private Integer quantity;
}