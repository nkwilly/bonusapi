package com.systemedebons.bonification.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import lombok.Data;


@Data
@Table("clients")
public class Client {

    @PrimaryKey
    private String id;

    @Column
    private String login;

    @Column("user_id")
    private String userId;
}

