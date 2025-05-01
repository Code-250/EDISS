package customerservice.service;

import customerservice.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/*
 * KafkaProducerService.java
 * the KafkaProducerService class is responsible for sending customer events to a Kafka topic.
 * It uses the KafkaTemplate to send messages asynchronously.
 * The class is annotated with @Service, indicating that it is a Spring service component.
 * The class is configured to use a Kafka topic specified in the application properties.
 * The sendCustomerEvent method sends a Customer object to the Kafka topic.
 * It handles exceptions that may occur during the sending process and logs the results.
 * The class is designed to be used in a Spring Boot application with Kafka integration.
 */
@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    @Value("${kafkaTopic}")
    private String KAFKA_TOPIC;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Constructor for KafkaProducerService
    // It initializes the KafkaTemplate used for sending messages to Kafka.
    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /*
     * sendCustomerEvent method
     * This method sends a Customer object to the Kafka topic.
     * It uses the KafkaTemplate to send the message asynchronously.
     * The method handles exceptions that may occur during the sending process and
     * logs the results.
     * 
     * @param customer The Customer object to be sent to the Kafka topic.
     * It is expected to be a JSON object.
     * 
     * @return void
     * 
     * @throws Exception if an error occurs while sending the message.
     */
    public void sendCustomerEvent(Customer customer) {
        try {
            logger.info("Sending customer to Kafka topic: {}", KAFKA_TOPIC);

            // Send directly without a key
            kafkaTemplate.send(KAFKA_TOPIC, customer).get(); // Wait for send to complete

            logger.info("Successfully sent customer to Kafka");
        } catch (Exception e) {
            logger.error("Failed to send customer to Kafka", e);
        }
    }
}