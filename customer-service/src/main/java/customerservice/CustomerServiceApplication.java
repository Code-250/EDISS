package customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CustomerServiceApplication is the main entry point for the Spring Boot
 * application.
 * It initializes the application context and starts the embedded server.
 */
@SpringBootApplication
public class CustomerServiceApplication {

    /**
     * The main method to run the Spring Boot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

}