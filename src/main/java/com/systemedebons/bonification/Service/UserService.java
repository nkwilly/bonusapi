package com.systemedebons.bonification.Service;


import com.systemedebons.bonification.Auth.JwtUtil;
import com.systemedebons.bonification.Auth.LoginRequest;
import com.systemedebons.bonification.Auth.LoginResponse;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.UserRepository;
import io.micrometer.observation.ObservationFilter;
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
    private JwtUtil jwtUtil;


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

            if(user.getMotDePasse() != null && !user.getMotDePasse().isEmpty()) {
                user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
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
            user.setMotDePasse(passwordEncoder.encode(newPassword));
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
            updatedUser.setNom(user.getNom());
            updatedUser.setPrenom(user.getPrenom());
            updatedUser.setEmail(user.getEmail());
            if (user.getMotDePasse() != null && !user.getMotDePasse().isEmpty()) {
                updatedUser.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
            }
            userRepository.save(updatedUser);
            return Optional.of(updatedUser);
        } else {
            return Optional.empty();
        }
    }


    /*public Optional<LoginResponse> login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getMotDePasse())) {
                String token = jwtUtil.generateToken();
                return Optional.of(new LoginResponse(token));
            }
        }
        return Optional.empty();
    }*/
}
