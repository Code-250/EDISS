package customerservice.entity;

import customerservice.dto.CustomerDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Customer is an entity class representing a customer in the system.
 * It contains customer details such as user ID, name, phone, address, etc.
 */
@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String name;
    private String phone;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zipcode;

    /**
     * Default constructor for Customer.
     */
    public Customer() {
    }

    /**
     * Constructor to create a Customer entity from a CustomerDTO.
     *
     * @param customerDTO The CustomerDTO containing customer details.
     */
    public Customer(CustomerDTO customerDTO) {
        this.id = customerDTO.getId();
        this.userId = customerDTO.getUserId();
        this.name = customerDTO.getName();
        this.phone = customerDTO.getPhone();
        this.address = customerDTO.getAddress();
        this.address2 = customerDTO.getAddress2();
        this.city = customerDTO.getCity();
        this.state = customerDTO.getState();
        this.zipcode = customerDTO.getZipcode();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipcode() {
        return zipcode;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}