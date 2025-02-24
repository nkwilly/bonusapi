package com.systemedebons.bonification.payload.response;

import com.systemedebons.bonification.Entity.Statuts;
import lombok.Data;

@Data
public class SavedTransactionResponse {
    private String id;

    private double amount;

    private Statuts statuts;

    private String clientLogin;

    private Boolean isDebit;

    private String message;

}
