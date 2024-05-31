package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Reward")
public class Reward {


    @Id
    private String id;
    private String nom;
    private String description;
    private int points;
}
