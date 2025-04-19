package com.bookstore.webbff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI webBffOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Web BFF API")
                        .description("Backend for Frontend API for web clients")
                        .version("1.0")
                        .contact(new Contact()
                                .name("BookStore Team")
                                .email("bookstore@example.com")));
    }
}