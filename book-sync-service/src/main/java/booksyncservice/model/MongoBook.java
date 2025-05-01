package booksyncservice.model;

import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books_rmunyema")
@Data
public class MongoBook {
    @Id
    private String _id;
    private String isbn;
    private String title;
    private String author;
    private String description;
    private String genre;
    private Double price;
    private Integer quantity;

    // Constructor to convert from RDS book
    public MongoBook(RdsBook rdsBook) {
        this._id = rdsBook.getIsbn();
        this.isbn = rdsBook.getIsbn();
        this.title = rdsBook.getTitle();
        this.author = rdsBook.getAuthor();
        this.description = rdsBook.getDescription();
        this.genre = rdsBook.getGenre();
        this.price = rdsBook.getPrice();
        this.quantity = rdsBook.getQuantity();
    }

    // Default constructor needed for MongoDB
    public MongoBook() {}
}