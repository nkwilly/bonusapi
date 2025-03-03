package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("rules")
public class Rule {

    @PrimaryKey
    private String id;

    @Column
    private String description;

    @Column("amount_max")
    private double amountMin;

    @Column("amount_min")
    private double amountMax;

    @Column
    private Integer points;

    @Column("min_days_for_irregular_clients")
    private Integer minDaysForIrregularClients;

    @Column("always_credit")
    private Boolean alwaysCredit;

    @Column("user_id")
    private String userId;
}
