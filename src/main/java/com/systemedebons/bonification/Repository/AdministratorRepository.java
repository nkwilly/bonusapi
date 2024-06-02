package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Administrator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends MongoRepository<Administrator, String> {

            Administrator findByUsername(String Username);
    Optional<Administrator> findByEmail(String email);
    Optional<Administrator> findByResetToken(String token);
}
