package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "history")
public class History {
    @Id
    private String id;

    private Integer points;

    private LocalDate date;
    
    @DBRef
    private Transaction transaction;
}