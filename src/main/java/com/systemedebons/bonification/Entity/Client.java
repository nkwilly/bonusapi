package com.systemedebons.bonification.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clients")
public class Client {

    @Id
    private String id;

    @Indexed(unique = true)
    private String login;

    @DBRef
    private User user;
}
