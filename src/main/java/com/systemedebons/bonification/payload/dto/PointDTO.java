package com.systemedebons.bonification.payload.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PointDTO {
    private String id;

    private Integer number;

    private LocalDate date;

    private String clientLogin;
}
