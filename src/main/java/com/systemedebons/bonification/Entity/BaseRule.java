package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("base_rule")
public class BaseRule {

    @PrimaryKey
    private String id;

    @Column("amount")
    private Double amount;

    @Column("user_id")
    private String userId;
}