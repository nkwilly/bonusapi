package com.systemedebons.bonification.Entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.systemedebons.bonification.Security.Deserializer.GrantedAuthorityDeserializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "utilisateurs")
public class User {

    @Id
    private String id; //Id de l'API qui utilise la notre

    @NotBlank
    @Size(max = 20)
    private String login; //Login de l'API

    @Email(message = "Email doit être valide")
    @Size(max = 50)
    @NotBlank(message = "L'email est obligatoire")
    private String email; //email par laquelle nous pourrions contacter les proprietaires de l'API qui utilisera la notre

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    private String resetToken;

    @JsonCreator
    public User(@JsonProperty("login") String login,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        this.login = login;
        this.email = email;
        this.password = password;

    }


    //@Override
    //public String getLogin() {
    //    return login;
    //}

}
