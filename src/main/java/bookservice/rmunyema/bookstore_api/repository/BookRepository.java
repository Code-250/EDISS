package bookservice.rmunyema.bookstore_api.repository;

import bookservice.rmunyema.bookstore_api.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}