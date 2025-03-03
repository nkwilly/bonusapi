package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("history")
public class History {

    @PrimaryKey
    private String id;

    @Column
    private Integer points;

    @Column
    private LocalDate date;

    @Column("transaction_id")
    private String transactionId;

    @Column("client_id")
    private String clientId;

    @Column("user_id")
    private String userId;
}
