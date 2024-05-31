package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "historiques")
public class Historique {
    @Id
    private String id;
    private String UserId;
    private LocalDate date;
    private String type;
    private int points;
    private  float montantTransaction;
}
