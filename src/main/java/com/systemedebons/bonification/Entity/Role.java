package com.systemedebons.bonification.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("roles")
public class Role {

    @PrimaryKey
    private String id;

    @Column
    @NotBlank
    private String name;
}
