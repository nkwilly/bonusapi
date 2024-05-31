package com.systemedebons.bonification.Entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collation = "transactions")
public class Transaction {

    @Id
    private String id;
    private LocalDate date;
    private float montant;
    private String type;
    private String statut;



}
