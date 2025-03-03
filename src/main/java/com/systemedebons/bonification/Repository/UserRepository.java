package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends CassandraRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String token);

    Optional<User> findByLogin(String login);

    Boolean existsByLogin(String login);

    Boolean existsByEmail(String email);
}
