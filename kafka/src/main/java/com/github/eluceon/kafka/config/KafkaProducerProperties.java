package com.github.eluceon.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka-service.kafka.producer")
public class KafkaProducerProperties {
    /**
     * Адреса для подключения к кафке
     */
    private String bootstrapServers;

    /**
     * Название топика
     */
    private String topic;
}
