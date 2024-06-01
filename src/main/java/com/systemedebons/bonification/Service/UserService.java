package com.systemedebons.bonification.Service;


import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {

        return userRepository.findById(id);

    }


    public  User saveUser(User user) {

        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
        return userRepository.save(user);
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }






}
