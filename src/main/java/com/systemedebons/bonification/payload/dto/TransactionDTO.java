package com.systemedebons.bonification.payload.dto;

import com.systemedebons.bonification.Entity.Statuts;
import lombok.Data;

@Data
public class TransactionDTO {
    private String id;

    private double amount;

    private Statuts status;

    private String clientLogin;

    private Boolean isDebit;
}
