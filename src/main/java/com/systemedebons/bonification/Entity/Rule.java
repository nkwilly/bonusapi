package com.systemedebons.bonification.Entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "rules")
public class Rule {

    @Id
    private String id;

    private String description;

    private double amountMin;

    private double amountMax;

    private Integer points;

    private Integer minDaysForIrregularClients;

    private Boolean alwaysCredit;

    @DBRef
    private User user;
}