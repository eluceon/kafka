package com.github.eluceon.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka-service.kafka.consumer")
public class KafkaConsumerProperties {
    /**
     * Макс. количество concurrent listenerContainer'ов
     */
    private Integer consumerConcurrency;

    /**
     * Адреса для подключения к кафке
     */
    private String bootstrapServers;

    /**
     * Идентификатор группы
     */
    private String groupId;

    /**
     * Слушаемые топики
     */
    private List<String> topics;

    /**
     * Auto offset reset
     */
    private String autoOffsetReset;

    /**
     * Таймаут сессии
     */
    private Integer sessionTimeoutMs;
}
