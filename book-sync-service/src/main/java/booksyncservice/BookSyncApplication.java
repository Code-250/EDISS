package booksyncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("booksyncservice.model")
public class BookSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookSyncApplication.class, args);
    }
}