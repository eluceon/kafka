package com.github.eluceon.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.eluceon.kafka.config.properties.KafkaProducerProperties;
import com.github.eluceon.kafka.dto.ServiceStatusEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer<ServiceStatusEventDto> {
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProducerProperties kafkaProperties;

    @Override
    public void send(ServiceStatusEventDto event) {
        try {
            String message = MAPPER.writeValueAsString(event);

            log.info("Send service status to topic {} -> {}", kafkaProperties.getTopic(), message);
            kafkaTemplate.send(kafkaProperties.getTopic(), message).addCallback(
                    result -> log.info("Message sent successfully to topic: {}, partition: {}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition()),
                    ex -> log.error("Failed to send message: ", ex)
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize service status to JSON", e);
        }
    }
}
