package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Rewards")
public class Reward {


    @Id
    private String  id;
    private String nom;
    private String description;
    private int points;
    @DBRef
    private Client client;
}
