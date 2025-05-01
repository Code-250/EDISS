package bookservice.repository;

import bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Book entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface BookRepository extends JpaRepository<Book, String> {
}