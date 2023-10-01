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
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer implements MessageListener<String, String> {
    private final EventHandler<EventDto> eventHandler;
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    private static final TypeReference<List<EventDto>> EVENT_TYPE_REF = new TypeReference<>() {
    };

    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        log.debug("Message from topic = [{}] processing started", data.topic());
        final List<EventDto> events = toEvents(data);

        events.forEach(this::processEvent);
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

        log.debug("Received message: [{}]", message.value());

        try {
            return MAPPER.readValue(value, EVENT_TYPE_REF);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert value to {}. cause = [{}], values = [{}], topic = [{}]",
                    EventDto.class, e.getMessage(), value, message.topic());
            return Collections.emptyList();
        }
    }
}
