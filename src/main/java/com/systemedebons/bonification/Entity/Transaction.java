package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("transactions")
public class Transaction {

    @PrimaryKey
    private String id;

    @Column
    private double amount;

    @Column
    private Statuts status;

    @Column("client_login")
    private String clientLogin;

    @Column("user_id")
    private String userId;

    @Column("is_debit")
    private Boolean isDebit;
}

