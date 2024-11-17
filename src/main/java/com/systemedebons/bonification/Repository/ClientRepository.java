package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    Optional<Client> findByEmail(String email);
    Optional<Client> findByResetToken(String token);
    Optional<Client> findByUsername(String Clientname);
    Boolean existsByUsername(String Clientname);
    Boolean existsByEmail(String email);

}
