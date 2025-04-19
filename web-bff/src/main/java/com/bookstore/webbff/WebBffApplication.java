package com.bookstore.webbff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * WebBffApplication is the main entry point for the Spring Boot application.
 * It initializes the application context and configures the RestTemplate bean.
 */
@SpringBootApplication
public class WebBffApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(WebBffApplication.class, args);
    }

    /**
     * Configures a RestTemplate bean for making HTTP requests.
     *
     * @param builder The RestTemplateBuilder used to create the RestTemplate.
     * @return A RestTemplate instance configured with the provided builder.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}