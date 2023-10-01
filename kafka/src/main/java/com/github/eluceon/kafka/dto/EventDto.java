package com.github.eluceon.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private String code;
    private StatusType status;
    private ZonedDateTime dateTime;
}
