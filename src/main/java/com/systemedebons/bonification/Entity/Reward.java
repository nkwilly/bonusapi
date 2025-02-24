package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "rewards")
public class Reward {
    @Id
    private String  id;

    private Double value;

    @DBRef
    @Indexed(unique = true)
    private User user;
}