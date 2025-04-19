package customerservice.repository;

import customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * CustomerRepository is an interface for performing CRUD operations on the
 * Customer entity.
 * It extends JpaRepository to provide basic CRUD functionality.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(String userId);
}
