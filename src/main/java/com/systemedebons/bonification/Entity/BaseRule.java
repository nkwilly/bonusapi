package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class    BaseRule {
    @Id
    private String id;

    private Double amount;

    @DBRef
    private User user;
}
