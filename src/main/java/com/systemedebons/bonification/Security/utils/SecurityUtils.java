package com.systemedebons.bonification.Security.utils;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.Role;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Entity.UserRoles;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.RoleRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Repository.UserRoleRepository;
import lombok.AllArgsConstructor;
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
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private UserRepository userRepository;

    private ClientRepository clientRepository;

    public Optional<Authentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public Optional<String> getCurrentUsername() {
        return getCurrentAuthentication().map(Principal::getName);
    }

    public Set<GrantedAuthority> getCurrentAuthorities() {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isPresent())
            return userRoleRepository.findByUserId(optionalUser.get().getId())
                    .stream().map(UserRoles::getRoleId)
                    .map(roleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Role::getName)
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        throw new RuntimeException("User has not authorities yet");
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

    public boolean isClientOfCurrentUser(String clientLogin) {
        Client client = clientRepository.findByLogin(clientLogin).orElse(new Client());
        User user = getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId().equals(client.getUserId());
    }

    public boolean isUserOfCurrentUser(String userId) {
        return getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId().equals(userId);
    }

    public boolean isCurrentUserAdmin() {
        return getCurrentAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","))
                .contains("ROLE_ADMIN");
    }
}
