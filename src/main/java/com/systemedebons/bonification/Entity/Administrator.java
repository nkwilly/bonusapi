package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admins")
@Data
public class Administrator {

  @Id
 private String administratorId;
  private String nom;
  private String prenom;
  private String  username;
  private String  email;
  private String  motDePasse;


}
