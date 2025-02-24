package com.systemedebons.bonification.payload.dto;

import com.systemedebons.bonification.Entity.Statuts;
import lombok.Data;

import java.time.LocalDate;

@Data
    public class TransactionHistoryDTO {
        private String id;

        private double amount;

        private Statuts status;

        private String clientLogin;

        private Boolean isDebit;

        private LocalDate date;

        private Integer points;
    }
