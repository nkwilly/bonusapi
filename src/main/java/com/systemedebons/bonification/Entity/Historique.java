package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "historiques")
public class Historique {
    @Id
    private String id;

    @DBRef
    private Client client;

    @DBRef
    private Transaction transaction;

    private int points;

    private String description;
}
