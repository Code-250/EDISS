package com.bookstore.mobilebff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/*
 * MobileBffApplication.java
 * This class is a Spring Boot application that serves as the entry point for the
 * application.
 * It is responsible for starting the application and configuring the necessary
 * beans.
 * The application excludes the DataSourceAutoConfiguration to avoid
 * auto-configuration of a data source.
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MobileBffApplication {

    /*
     * main() method is the entry point of the Spring Boot application.
     * It starts the application by calling SpringApplication.run() method.
     * 
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(com.bookstore.mobilebff.MobileBffApplication.class, args);
    }

    /*
     * restTemplate() method creates a RestTemplate bean that can be used to make
     * HTTP requests.
     * It uses RestTemplateBuilder to build the RestTemplate instance.
     * 
     * @param builder The RestTemplateBuilder used to create the RestTemplate.
     * 
     * @return A RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}