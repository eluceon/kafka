package com.github.eluceon.kafka.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kafka-service.kafka.consumer")
@Getter
@Setter
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

    private Ssl ssl;
    private Security security;

    @Getter
    @Setter
    public static class Ssl {
        private Boolean enabled;
        private String truststoreLocation;
        private String truststorePassword;
        private String keystoreLocation;
        private String keystorePassword;
        private String keyPassword;
        private String keystoreType;
        private String truststoreType;
    }

    @Getter
    @Setter
    public static class Security {
        private String protocol;
    }
}
