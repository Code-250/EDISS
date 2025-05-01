package booksyncservice.repository;

import booksyncservice.model.MongoBook;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MongoBookRepository extends MongoRepository<MongoBook, String> {
    @Query(value = "{}", collation = "rmunyema")
    List<MongoBook> findAll();

    @Query(value = "{_id: ?0}", collation = "rmunyema")
    Optional<MongoBook> findById(String id);
}