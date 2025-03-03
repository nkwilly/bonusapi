package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Role;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

public interface RoleRepository extends CassandraRepository<Role, String> {
    Optional<Role> findByName(@NotBlank String name);
}