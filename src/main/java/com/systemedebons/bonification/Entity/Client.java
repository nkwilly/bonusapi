package com.systemedebons.bonification.Entity;

import org.springframework.data.annotation.Id;
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
    private String id; //id du client dans notre base de donnees

    private String username; //id du client dans la base de donne de l'API d'origine 

    private String userId; //id de l'API qui a stocke ce client


}
