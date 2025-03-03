package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("points")
public class Point {

    @PrimaryKey
    private String id;

    @Column
    private Integer number;

    @Column("client_id")
    private String clientId;

    @Column("user_id")
    private String userId;
}

