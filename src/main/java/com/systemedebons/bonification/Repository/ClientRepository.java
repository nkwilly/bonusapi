package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ClientRepository extends CassandraRepository<Client, String> {
    Optional<Client> findByLogin(String login);

    List<Client> findByUserId(String userId);

    void deleteByLogin(String login);
}
