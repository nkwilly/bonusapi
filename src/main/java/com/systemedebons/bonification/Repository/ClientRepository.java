package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByClientName(String Clientname);
    List<Client> findByUserId(String userId);
    Optional<Client> findByClientNameAndUserId(String clientName, String userId);
}
