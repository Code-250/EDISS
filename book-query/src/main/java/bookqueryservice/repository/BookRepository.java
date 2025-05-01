package bookqueryservice.repository;

import bookqueryservice.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
/**
 * Repository interface for managing Book entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */


public interface BookRepository extends MongoRepository<Book, String> {
    // Find by exact ISBN
    Book findByIsbn(String isbn);

    // Text search
    @Query("{ $text: { $search: ?0 } }")
    List<Book> findByKeyword(String keyword);
}
