package booksyncservice.repository;

import booksyncservice.model.RdsBook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RdsBookRepository extends JpaRepository<RdsBook, String> {
    List<RdsBook> findAll();
}