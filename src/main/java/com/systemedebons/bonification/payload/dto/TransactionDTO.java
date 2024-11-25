package com.systemedebons.bonification.payload.dto;

import com.systemedebons.bonification.Entity.Statuts;
import lombok.Data;

@Data
public class TransactionDTO {

    private float montant;

    private String type;

    private Statuts status;

    private String clientId;

}
