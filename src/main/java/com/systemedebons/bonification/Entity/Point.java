package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "points")
public class Point {

    @Id
    private String id;

    private Integer number;

    @DBRef
    private Client client;
}
