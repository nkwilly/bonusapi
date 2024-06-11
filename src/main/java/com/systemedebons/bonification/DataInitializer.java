package com.systemedebons.bonification;
import com.systemedebons.bonification.Entity.ERole;
import com.systemedebons.bonification.Entity.Role;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.RoleRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        addRoleIfNotExists(ERole.ROLE_USER);
        addRoleIfNotExists(ERole.ROLE_ADMIN);
        addRoleIfNotExists(ERole.ROLE_MODERATOR);

        addDefaultUser();
    }

    private void addRoleIfNotExists(ERole role) {
        if (roleRepository.findByName(role).isEmpty()) {
            Role newRole = new Role();
            newRole.setName(role);
            roleRepository.save(newRole);
        }
    }

    private void addDefaultUser() {
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User("defaultuser", "NomDefault", "PrenomDefault", "defaultuser@example.com", passwordEncoder.encode("password"));
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_USER).get());
            roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).get());
            user.setRoles(roles);
            userRepository.save(user);
        }
    }
}