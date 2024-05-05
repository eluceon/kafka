package com.github.eluceon.kafka.config;

import com.github.eluceon.kafka.config.properties.KafkaProducerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfiguration {
    private final KafkaProducerProperties properties;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    Map<String, Object> producerConfigs() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, properties.getMaxRequestSize());
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, properties.getBatchSize());
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, properties.getLingerMs());
        configProps.put(ProducerConfig.ACKS_CONFIG, properties.getAcks());
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, properties.getClientId());

        return configProps;
    }
}
