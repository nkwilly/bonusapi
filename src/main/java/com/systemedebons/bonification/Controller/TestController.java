package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.ERole;
import com.systemedebons.bonification.Entity.Role;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.RoleRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private SecurityUtils securityUtils;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @PostMapping("/create_role/user")
    public ResponseEntity<Role> createUserRole() {
        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        return new ResponseEntity<>(roleRepository.save(role), HttpStatus.CREATED);
    }

    @PostMapping("/create_role/admin")
    public ResponseEntity<Role> createAdminRole() {
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        return new ResponseEntity<>(roleRepository.save(role), HttpStatus.CREATED);
    }

    @GetMapping("/role/admin")
    public ResponseEntity<Role> getAdminRole() {
        return ResponseEntity.ok(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow());
    }

    @GetMapping("/role/user")
    public ResponseEntity<Role> getUserRole() {
        return ResponseEntity.ok(roleRepository.findByName(ERole.ROLE_USER).orElseThrow());
    }

    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo() {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> {
            log.error("User not logged in");
            return new EntityNotFound("User not logged in");
        });
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/is_admin")
    public ResponseEntity<Boolean> isAdmin() {
        return ResponseEntity.ok(securityUtils.isCurrentUserAdmin());
    }
}