package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.ERole;
import com.systemedebons.bonification.Entity.Role;
import com.systemedebons.bonification.Repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/role")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RoleController {
    private static final Logger log = LoggerFactory.getLogger(RoleController.class);

    private RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity<Role> createUserRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleRepository.save(role));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<Role> getRoleByName(@RequestParam ERole role) {
        return ResponseEntity.of(roleRepository.findByName(role));
    }

    @PutMapping
    public ResponseEntity<Role> updateRole(@RequestParam ERole oldRole, @RequestParam ERole newRole) {
        Role role = roleRepository.findByName(oldRole).orElse(new Role());
        role.setName(newRole.toString());
        return ResponseEntity.ok(roleRepository.save(role));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRole(@RequestParam ERole role) throws RoleNotFoundException {
        Role deleleRole = roleRepository.findByName(role).orElseThrow(() -> new RoleNotFoundException(role.toString()));
        roleRepository.delete(deleleRole);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
