package com.github.eluceon.kafka.config.properties;

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
     * Идентификатор клиента
     */
    private String clientId;

    /**
     * Максимальный размер запроса в байтах.
     * Определяет верхний предел размера сообщения, которое может отправить продюсер.
     */
    private Integer maxRequestSize;

    /**
     * Размер пакета данных для продюсера Kafka в байтах.
     */
    private Integer batchSize;

    /**
     * Время ожидания в миллисекундах перед отправкой пакета данных.
     * Если пакет не достигает максимального размера batch.size, продюсер
     * будет ожидать указанное время перед отправкой пакета.
     */
    private Integer lingerMs;

    /**
     * Уровень подтверждения для продюсера Kafka.
     * Определяет, какое количество подтверждений от брокеров необходимо для считывания сообщения доставленным.
     * 0 - продюсер не ждёт подтверждений, максимальная скорость отправки, риск потери данных.
     * 1 - требуется подтверждение от лидер-брокера.
     * -1 - требуются подтверждения от всех реплик.
     */
    private String acks;

    /**
     * Название топика
     */
    private String topic;
}
