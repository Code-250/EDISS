package bookservice.rmunyema.bookstore_api.customerService.repository;

import bookservice.rmunyema.bookstore_api.customerService.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(String userId);
}
