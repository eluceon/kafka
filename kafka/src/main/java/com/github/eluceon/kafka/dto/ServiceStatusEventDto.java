package com.github.eluceon.kafka.dto;

import com.github.eluceon.handler.dto.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatusEventDto {
    private String service;
    private StatusType status;
}
