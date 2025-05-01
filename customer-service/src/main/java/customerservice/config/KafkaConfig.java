package customerservice.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.*;

/**
 * KafkaConfig.java
 * This class is a Spring Boot configuration class that sets up the Kafka
 * producer
 * configuration.
 * It defines the Kafka producer factory and Kafka template beans.
 * The class uses the @Configuration annotation to indicate that it provides
 * configuration for the Spring application context.
 * The Kafka producer factory is configured with the bootstrap servers and
 * serializers for key and value.
 */
@Configuration
public class KafkaConfig {
    @Value("${kafka.brokers}")
    private String BOOTSTRAP_SERVERS;

    /**
     * Kafka producer factory bean.
     * This method creates a ProducerFactory for Kafka producers.
     * It configures the producer with the bootstrap servers and serializers for key
     * and value.
     *
     * @return ProducerFactory<String, Object> configured Kafka producer factory.
     */
    @Bean
    public ProducerFactory<String, Object> kafkaProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Kafka template bean.
     * This method creates a KafkaTemplate for sending messages to Kafka topics.
     * It uses the configured producer factory.
     *
     * @return KafkaTemplate<String, Object> configured Kafka template.
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }
}