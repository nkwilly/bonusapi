package com.systemedebons.bonification.Service;


import com.systemedebons.bonification.Entity.ERole;
import com.systemedebons.bonification.Entity.Role;
import com.systemedebons.bonification.Entity.UserRoles;
import com.systemedebons.bonification.Repository.RoleRepository;
import com.systemedebons.bonification.Repository.UserRoleRepository;
import com.systemedebons.bonification.Security.Jwt.JwtUtils;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private EmailService emailService;

    private SecurityUtils securityUtils;

    private JwtUtils jwtUtils;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {

        return userRepository.findById(id);

    }

    public User saveUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("L'adresse e-mail est déjà utilisée.");
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            throw new IllegalArgumentException("Mot de Passe ne peut pas être null ou  vide");
        }
        User savedUser = userRepository.save(user);
        Role userRole = roleRepository.findByName(ERole.ROLE_USER.name()).orElseThrow(() -> new RuntimeException("User Role not exist"));
        UserRoles userRoles = new UserRoles();
        userRoles.setRoleId(userRole.getId());
        userRoles.setUserId(savedUser.getId());
        UserRoles savedUserRole = userRoleRepository.save(userRoles);
        log.info("user role saved: {}", savedUserRole);
        log.info("user saved {}", savedUser);
        return savedUser;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public void resetPassword(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User userSaved = user.get();
            String token = UUID.randomUUID().toString();
            userSaved.setResetToken(token);
            userRepository.save(userSaved);
            emailService.sendPasswordResetEmail(userSaved.getEmail(), token);
        } else {
            throw new IllegalArgumentException("L'adresse e-mail est introuvable ou non unique.");
        }
    }

    public void updatePassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Token invalide ou expiré");
        }
    }

    public Optional<User> updateUser(String id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(updatedUser);
            return Optional.of(updatedUser);
        } else {
            return Optional.empty();
        }
    }
}
