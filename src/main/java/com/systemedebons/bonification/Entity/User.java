package com.systemedebons.bonification.Entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("users")
public class User {

    @PrimaryKey
    private String id; // Id de l'API qui utilise la nôtre

    @Column
    @NotBlank
    @Size(max = 20)
    private String login; // Login de l'API

    @Column
    @Email(message = "Email doit être valide")
    @Size(max = 50)
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Column
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @Column("reset_token")
    private String resetToken;

    @JsonCreator
    public User(@JsonProperty("login") String login,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }
}
