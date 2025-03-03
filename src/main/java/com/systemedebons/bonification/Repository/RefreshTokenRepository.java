package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.RefreshToken;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CassandraRepository<RefreshToken, String> {

    Optional<RefreshToken> findByTokens(String token);
        void deleteByUserId(String userId);
    void deleteByTokens(String token);
    Optional<RefreshToken> findByUserId(String userId);
}
