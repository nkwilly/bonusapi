package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.ERole;
import com.systemedebons.bonification.Entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}