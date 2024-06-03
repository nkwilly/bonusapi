package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Auth.JwtUtil;
import com.systemedebons.bonification.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.systemedebons.bonification.Entity.User;

import java.util.*;

@Service
public class AuthService {
/***
@Autowired
   private UserRepository  userRepository;

@Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getMotDePasse())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

***/

@Autowired
private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return jwtUtil.generateToken(authentication);
    }
}
