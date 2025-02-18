package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private double amount;

    private Statuts status;

    @DBRef
    private Client client;

    private Boolean isDebit; // Si True alors on intègre le calcul des points dans la facturation et il y a donc débit de points.
}
