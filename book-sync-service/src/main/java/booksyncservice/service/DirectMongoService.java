package booksyncservice.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@EnableScheduling
public class DirectMongoService {
    private static final Logger logger = LoggerFactory.getLogger(DirectMongoService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("directMongoClient")
    private MongoClient mongoClient;

    @Value("${andrew.id}")
    private String andrewId;

    /**
     * Scheduled method to sync books from RDS to MongoDB
     * Runs every minute by default, can be configured via sync.interval.ms property
     */
    @Scheduled(fixedDelayString = "${sync.interval.ms:60000}")
    public void syncBooks() {
        try {
            logger.info("Starting book synchronization from RDS to MongoDB");
            String collectionName = "books_rmunyema";
            logger.info("Using collection name: {}", collectionName);

            // 1. Get MongoDB collection
            MongoCollection<Document> collection = mongoClient
                    .getDatabase("BooksDB")
                    .getCollection(collectionName);

            // 2. Fetch all books from RDS
            logger.info("Fetching books from RDS...");
            List<Map<String, Object>> rdsBooks = fetchBooksFromRDS();
            logger.info("Found {} books in RDS", rdsBooks.size());

            if (rdsBooks.isEmpty()) {
                logger.warn("No books found in RDS. Nothing to synchronize.");
                return;
            }

            // 3. Convert RDS books to MongoDB documents
            logger.info("Converting RDS books to MongoDB documents...");
            List<Document> mongoBooks = new ArrayList<>();
            for (Map<String, Object> rdsBook : rdsBooks) {
                Document doc = convertToMongoDocument(rdsBook);
                mongoBooks.add(doc);
                logger.info("Converted book: ISBN={}, Title={}",
                        doc.getString("isbn"), doc.getString("title"));
            }

            // 4. Save each book to MongoDB
            logger.info("Starting to save {} books to MongoDB...", mongoBooks.size());
            int savedCount = 0;
            for (Document book : mongoBooks) {
                try {
                    // Use _id as the key for upsert
                    String isbn = book.getString("_id");
                    logger.info("Saving book to MongoDB: _id={}, isbn={}, title={}",
                            isbn, book.getString("isbn"), book.getString("title"));

                    // Create a filter to find the document by _id
                    Document filter = new Document("_id", isbn);

                    // Delete existing document if it exists (to avoid partial updates)
                    collection.deleteOne(filter);

                    // Insert the new document
                    collection.insertOne(book);

                    savedCount++;
                    logger.info("Successfully saved book {}/{}: isbn={}",
                            savedCount, mongoBooks.size(), isbn);
                } catch (Exception e) {
                    String isbn = book.getString("_id");
                    logger.error("Error saving book {}: {}", isbn, e.getMessage());
                    // Continue with next book
                }
            }

            logger.info("Successfully synchronized {}/{} books to MongoDB",
                    savedCount, mongoBooks.size());
        } catch (Exception e) {
            logger.error("Error during book synchronization", e);
            logger.error("Stack trace:", e);
        }
    }

    /**
     * Fetches all books from RDS database
     */
    private List<Map<String, Object>> fetchBooksFromRDS() {
        try {
            String sql = "SELECT * FROM books";
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.error("Error fetching books from RDS: {}", e.getMessage());
            logger.error("RDS fetch stack trace:", e);
            return new ArrayList<>();
        }
    }

    /**
     * Converts an RDS book record to a MongoDB document
     */
    private Document convertToMongoDocument(Map<String, Object> rdsBook) {
        Document doc = new Document();

        // Set _id to ISBN for MongoDB
        String isbn = rdsBook.get("isbn").toString();
        doc.append("_id", isbn);

        // Add all fields from RDS
        doc.append("isbn", isbn);
        doc.append("title", rdsBook.get("title"));
        doc.append("author", rdsBook.get("author"));
        doc.append("description", rdsBook.get("description"));
        doc.append("genre", rdsBook.get("genre"));

        // Handle numeric types
        if (rdsBook.get("price") != null) {
            if (rdsBook.get("price") instanceof Double) {
                doc.append("price", (Double) rdsBook.get("price"));
            } else if (rdsBook.get("price") instanceof Integer) {
                doc.append("price", ((Integer) rdsBook.get("price")).doubleValue());
            } else {
                try {
                    doc.append("price", Double.parseDouble(rdsBook.get("price").toString()));
                } catch (NumberFormatException e) {
                    doc.append("price", 0.0);
                }
            }
        } else {
            doc.append("price", 0.0);
        }

        if (rdsBook.get("quantity") != null) {
            if (rdsBook.get("quantity") instanceof Integer) {
                doc.append("quantity", (Integer) rdsBook.get("quantity"));
            } else if (rdsBook.get("quantity") instanceof Double) {
                doc.append("quantity", ((Double) rdsBook.get("quantity")).intValue());
            } else {
                try {
                    doc.append("quantity", Integer.parseInt(rdsBook.get("quantity").toString()));
                } catch (NumberFormatException e) {
                    doc.append("quantity", 0);
                }
            }
        } else {
            doc.append("quantity", 0);
        }

        return doc;
    }
}