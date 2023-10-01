package com.github.eluceon.api.controller;

import com.github.eluceon.api.dto.ServiceStatusRequestDto;
import com.github.eluceon.api.mapper.ServiceStatusMapper;
import com.github.eluceon.kafka.dto.ServiceStatusEventDto;
import com.github.eluceon.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final ServiceStatusMapper mapper;
    private final KafkaProducer<ServiceStatusEventDto> kafkaProducer;

    @PostMapping("/status")
    public ResponseEntity<Void> postStatus(@Valid @RequestBody ServiceStatusRequestDto request) {
        log.debug("Received request with RqUID: {}", request.getRqUID());

        kafkaProducer.send(mapper.requestToEvent(request));
        return ResponseEntity.ok().build();
    }
}
