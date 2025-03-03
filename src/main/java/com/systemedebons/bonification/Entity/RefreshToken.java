package com.systemedebons.bonification.Entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Data
@Table("refresh_token")
public class RefreshToken {

    @PrimaryKey
    private String id;

    @Column("user_id")
    private String userId;

    @Column
    private String tokens;

    @Column("expiry_date")
    private Instant expiryDate;

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }
}
