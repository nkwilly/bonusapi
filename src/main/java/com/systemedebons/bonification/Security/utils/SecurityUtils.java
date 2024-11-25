package com.systemedebons.bonification.Security.utils;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);
    private UserRepository userRepository;

    private ClientRepository clientRepository;

    public Optional<Authentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public Optional<String> getCurrentUsername() {
        return getCurrentAuthentication().map(Principal::getName);
    }

    public Set<GrantedAuthority> getCurrentAuthorities() {
        return getCurrentUser().map(User::getRoles).
                stream().map(elt -> new SimpleGrantedAuthority(elt.toString()))
                .collect(Collectors.toSet());
    }

    public Optional<Object> getCurrentPrincipal() {
        return getCurrentAuthentication().map(elt -> elt.getPrincipal());
    }

    public boolean isAuthenticated() {
        return getCurrentAuthentication().isPresent();
    }

    public Optional<User> getCurrentUser() {
        String username = getCurrentUsername().orElse("");
        return userRepository.findByLogin(username);
    }

    public boolean isClientOfCurrentUser(String clientId) {
        Client client = clientRepository.findById(clientId).orElse(new Client());
        User user = getCurrentUser().orElse(new User());
        return user.getId().equals(client.getUserId());
    }

    public boolean isUserOfCurrentUser(String userId) {
        return getCurrentUser()
                .orElse(new User())
                .getId().equals(userId);
    }

    public boolean isCurrentUserAdmin() {
        return getCurrentAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","))
                .contains("ROLE_ADMIN");
    }
}
