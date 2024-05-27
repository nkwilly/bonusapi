package com.systemedebons.bonification.Entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collation = "Rules")
public class Rule {

    @Id
    private String id;

    private float minAmount;

    private float maxAmount;

    private int PointsToAttribute;
}
