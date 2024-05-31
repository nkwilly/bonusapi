package com.systemedebons.bonification.Entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "utilisateurs")
public class User {

    @Id
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

}
