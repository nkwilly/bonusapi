package com.systemedebons.bonification.payload.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class RuleDTO {
    private String description;

    private Double amountMin;

    private Integer points;

    private Integer minDaysForIrregularClients;

    private Boolean alwaysCredit;
}
