package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Role;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Entity.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends CassandraRepository<UserRoles, String> {
    Optional<UserRoles> findByRoleId(String role);

    List<UserRoles> findByUserId(String userId);

    default List<Role> findByUserIdAndGetRoles(String userId, RoleRepository roleRepository) {
        List<UserRoles> userRoles = this.findByUserId(userId);
        List<Role> roles = new ArrayList<>();
        for (UserRoles userRole : userRoles) {
            roles.add(roleRepository.findById(userRole.getRoleId()).orElse(null));
        }
        return roles;
    }
}
