package customerservice.dto;

import customerservice.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * CustomerDTO is a Data Transfer Object representing customer details.
 * It is used to transfer customer data between the client and server.
 */
public class CustomerDTO {
    private Long id;
    @NotNull
    @Email
    private String userId;
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private String address;
    private String address2;
    @NotNull
    private String city;
    @NotNull
    @Pattern(regexp = "[A-Z]{2}")
    private String state;
    @NotNull
    private String zipcode;

    /**
     * Default constructor for CustomerDTO.
     */
    public CustomerDTO() {
    }

    /**
     * Constructor to create a CustomerDTO from a Customer entity.
     *
     * @param customer The Customer entity to convert to DTO.
     */
    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.userId = customer.getUserId();
        this.name = customer.getName();
        this.phone = customer.getPhone();
        this.address = customer.getAddress();
        this.address2 = customer.getAddress2();
        this.city = customer.getCity();
        this.state = customer.getState();
        this.zipcode = customer.getZipcode();
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