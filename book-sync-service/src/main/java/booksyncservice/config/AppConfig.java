package booksyncservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "booksyncservice.repository")
@EnableJpaRepositories(basePackages = "booksyncservice.repository")
public class AppConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${andrew.id}")
    private String andrewId;

    @Bean(name = "directMongoClient")
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoCollection<Document> booksCollection() {
        return mongoClient()
                .getDatabase("BooksDB")
                .getCollection("books_rmunyema");
    }
}