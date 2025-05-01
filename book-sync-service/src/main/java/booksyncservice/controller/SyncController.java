package booksyncservice.controller;

import booksyncservice.service.DirectMongoService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SyncController {
    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    private DirectMongoService directMongoService;

    @Autowired
    private MongoCollection<Document> booksCollection;

    @Value("${andrew.id}")
    private String andrewId;

    /**
     * Endpoint to manually trigger book synchronization
     */
    @GetMapping("/sync/trigger")
    public ResponseEntity<Map<String, String>> triggerSync() {
        logger.info("Manual sync triggered via API");
        try {
            directMongoService.syncBooks();
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Book synchronization completed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during manual sync: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Sync failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Endpoint to get service status
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Book Sync Service is UP");
    }

    /**
     * Endpoint to view books in MongoDB
     */
    @GetMapping("/mongo/books")
    public ResponseEntity<?> getMongoBooks() {
        logger.info("Getting all books from MongoDB collection");
        try {
            List<Document> books = new ArrayList<>();
            FindIterable<Document> documents = booksCollection.find().limit(100);

            for (Document doc : documents) {
                books.add(doc);
            }

            logger.info("Found {} books in MongoDB", books.size());

            if (books.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            logger.error("Error retrieving books from MongoDB: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to retrieve books: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Endpoint to get MongoDB collection info
     */
    @GetMapping("/mongo/info")
    public ResponseEntity<?> getMongoInfo() {
        try {
            Map<String, Object> info = new HashMap<>();
            info.put("collectionName", andrewId);
            info.put("database", "BooksDB");

            // Get collection count
            long count = booksCollection.countDocuments();
            info.put("documentCount", count);

            return ResponseEntity.ok(info);
        } catch (Exception e) {
            logger.error("Error getting MongoDB info: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to get MongoDB info: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}