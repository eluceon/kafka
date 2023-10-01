package com.github.eluceon.kafka.producer;

/**
 * Интерфейс для отправки событий в Kafka
 *
 * @param <T>
 */
public interface KafkaProducer<T>{

    /**
     * Отправляет событие
     *
     * @param event событие для отправки
     */
    void send(T event);
}
