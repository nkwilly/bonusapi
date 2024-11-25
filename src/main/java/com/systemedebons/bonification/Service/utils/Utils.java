package com.systemedebons.bonification.Service.utils;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class Utils {
    private UserRepository userRepository;

    private ClientRepository clientRepository;

    public boolean clientForUser(User user, Client client) {
        return client.getUserId().equals(user.getId());
    }

    public boolean clientForUser(String username, String clientId) {
        User user = userRepository.findByLogin(username).orElse(new User());
        Client client = clientRepository.findById(clientId).orElse(new Client());
        return clientForUser(user, client);
    }

    public boolean userForUser(User currentUser, User otherUser) {
        return currentUser.getId().equals(otherUser.getId());
    }

    public boolean userForUser(String currentUsername, String otherUsername) {
        User currentUser = userRepository.findByLogin(currentUsername).orElse(new User());
        User otherUser = userRepository.findByLogin(otherUsername).orElse(new User());
        return userForUser(currentUser, otherUser);
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId);
    }
}
