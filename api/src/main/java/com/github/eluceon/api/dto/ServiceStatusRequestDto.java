package com.github.eluceon.api.dto;

import com.github.eluceon.handler.dto.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatusRequestDto {
    @NotNull(message = "rqUID cannot be null")
    @NotBlank(message = "rqUID cannot be blank")
    private String rqUID;
    @NotNull(message = "status cannot be null")
    private StatusType status;
    @NotNull(message = "service cannot be null")
    @NotBlank(message = "service cannot be blank")
    private String service;
}
