package com.github.eluceon.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.eluceon.handler.EventHandler;
import com.github.eluceon.handler.dto.EventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer implements AcknowledgingMessageListener<String, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    private static final TypeReference<List<EventDto>> EVENT_TYPE_REF = new TypeReference<>() {
    };

    private final EventHandler<EventDto> eventHandler;

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        try {
            logMetadata(data);
            final List<EventDto> events = toEvents(data);
            events.forEach(this::processEvent);
            acknowledgment.acknowledge();
            log.info("Processed message on topic {}: {}", data.topic(), data.value());
        } catch (Exception e) {
            log.error("Failed to process message on topic {}: {}", data.topic(), data.value(), e);
        }
    }

    private void processEvent(EventDto event) {
        try {
            log.debug("Processing event with parameters: code = [{}]", event.getCode());
            eventHandler.handle(event);
            log.debug("Complete processing event with parameters: code = [{}]", event.getCode());
        } catch (RuntimeException e) {
            log.error("Error processing event with parameters: code = [{}]", event.getCode());
        }
    }

    private List<EventDto> toEvents(ConsumerRecord<String, String> message) {
        final String value = message.value();

        if (value == null) {
            log.error("No value in record = [{}]. topic = [{}]", message, message.topic());
            return Collections.emptyList();
        }

        try {
            return MAPPER.readValue(value, EVENT_TYPE_REF);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert value to {}. cause = [{}], values = [{}], topic = [{}]",
                    EventDto.class, e.getMessage(), value, message.topic());
            return Collections.emptyList();
        }
    }

    private void logMetadata(ConsumerRecord<String, String> data) {
        LocalDateTime timestampAsDate =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(data.timestamp()), TimeZone.getDefault().toZoneId());

        log.info("Received data on topic {} (partition = {}, offset = {}, timestamp = {} ({}))",
                data.topic(), data.partition(), data.offset(), data.timestamp(), timestampAsDate);
    }
}

