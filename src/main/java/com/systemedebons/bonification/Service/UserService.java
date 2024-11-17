package com.systemedebons.bonification.Service;


import com.systemedebons.bonification.Security.Jwt.JwtUtils;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {

        return userRepository.findById(id);

    }


    public  User saveUser(User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("L'adresse e-mail est déjà utilisée.");
        }

            if(user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else{
                throw new IllegalArgumentException("Mot de Passe ne peut pas être null ou  vide");
            }
            User userSaved = userRepository.save(user);
            emailService.sendWelcomeEmail(user.getEmail());

        return userSaved;
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }


    public void resetPassword(String email) {


        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
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
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            userRepository.save(user);
        }else{
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
