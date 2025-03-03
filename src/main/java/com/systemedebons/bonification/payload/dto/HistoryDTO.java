package com.systemedebons.bonification.payload.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HistoryDTO {
    private String id;
    private Integer points;
    private LocalDate date;
    private String transactionId;
    private String clientId;
}
