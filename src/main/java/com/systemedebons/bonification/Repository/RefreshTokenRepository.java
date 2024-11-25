package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);
        void deleteByUserId(String userId);
    void deleteByToken(String token);
    Optional<RefreshToken> findByUserId(String userId);
}
