package bookqueryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main application class for the Book Service.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
public class BookQueryApplication {

    /**
     * Main method to run the Spring Boot application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BookQueryApplication.class, args);
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }
}