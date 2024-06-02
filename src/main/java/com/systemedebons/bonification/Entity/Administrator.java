package com.systemedebons.bonification.Entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admins")
@Data
public class Administrator {

  @Id
 private String administratorId;

 @NotBlank(message = "Le nom est obligatoire")
  private String nom;
 @NotBlank(message = "Le prénom est obligatoire")
  private String prenom;
  private String  username;
 @Email(message = "Email doit être valide")
 @NotBlank(message = "L'email nom est obligatoire")
  private String  email;
 @NotBlank(message = "Le mot de passe est obligatoire")
  private String  motDePasse;

 private String resetToken;

}
