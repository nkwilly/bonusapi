package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByLogin(String login);
    List<Client> findByUserId(String userId);
    void deleteByLogin(String login);
   // Optional<Client> findByLoginAndUserId(String clientLogin, String userId);
}
