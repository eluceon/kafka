package com.github.eluceon.api.mapper;

import com.github.eluceon.api.dto.ServiceStatusRequestDto;
import com.github.eluceon.kafka.dto.ServiceStatusEventDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceStatusMapper {
    ServiceStatusEventDto requestToEvent(ServiceStatusRequestDto request);
}
