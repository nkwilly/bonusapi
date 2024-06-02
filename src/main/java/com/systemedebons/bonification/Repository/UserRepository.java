package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String token);
    List<User> findAllByEmail(String email);

}
