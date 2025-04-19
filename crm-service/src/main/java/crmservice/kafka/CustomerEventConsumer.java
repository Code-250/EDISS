package crmservice.kafka;

import crmservice.entity.Customer;
import crmservice.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component

public class CustomerEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(CustomerEventConsumer.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafkaTopic}")
    public void listen(String message) {

        try {
            logger.info(message + " the message logger===>>");
            logger.info("Received customer event: {}", message);

            // Deserialize the JSON message to a Customer object

            Customer customer = objectMapper.readValue(message, Customer.class);

            // Send welcome email
            emailService.sendWelcomeEmail(customer);

        } catch (Exception e) {
            logger.error("Error processing customer event", e);
        }
    }
}