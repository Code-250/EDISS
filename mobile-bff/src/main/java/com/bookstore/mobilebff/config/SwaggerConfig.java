package com.bookstore.mobilebff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mobileBffOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mobile BFF API")
                        .description("Backend for Frontend API for mobile clients")
                        .version("1.0")
                        .contact(new Contact()
                                .name("BookStore Team")
                                .email("bookstore@example.com")));
    }
}