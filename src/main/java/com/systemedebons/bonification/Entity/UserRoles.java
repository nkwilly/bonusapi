package com.systemedebons.bonification.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("user_roles")
public class UserRoles {

    @PrimaryKey
    private String id;

    @Column("role_id")
    private String roleId;

    @Column("user_id")
    private String userId;
}
