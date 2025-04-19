package crmservice.service;

import crmservice.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${andrew.id}")
    private String andrewId;

    public void sendWelcomeEmail(Customer customer) {
        try {
            logger.info("Sending welcome email to: {}", customer.getUserId());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(customer.getUserId());
            message.setSubject("Activate your book store account");

            String body = String.format(
                    "Dear %s,\n\n" +
                            "Welcome to the Book store created by %s.\n" +
                            "Exceptionally this time we won't ask you to click a link to activate your account.",
                    customer.getName(), andrewId);

            message.setText(body);

            mailSender.send(message);
            logger.info("Welcome email sent successfully to: {}", customer.getUserId());
        } catch (Exception e) {
            logger.error("Failed to send welcome email to: {}", customer.getUserId(), e);
        }
    }
}