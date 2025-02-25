package com.systemedebons.bonification.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    @NotBlank
    private String name;
}