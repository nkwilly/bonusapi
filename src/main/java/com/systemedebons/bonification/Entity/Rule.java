package com.systemedebons.bonification.Entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Rules")
public class Rule {

    @Id
    private String id;
    private String typeRegle;
    private float montantMin;
    private int points;
}
