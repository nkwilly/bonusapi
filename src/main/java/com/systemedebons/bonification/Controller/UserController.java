package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.payload.request.LoginRequest;
import com.systemedebons.bonification.Service.UserService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "Utilisateur Management System", description = "Operations pertaining to user in User Management System")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable String id) {

        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@Valid  @RequestBody User user) {
        try {
            User  saveUser = userService.saveUser(user);
            return ResponseEntity.ok(saveUser);
        }catch (IllegalArgumentException e){

            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {

        try {
            userService.resetPassword(email);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token , @RequestParam String newPassword) {
    try{
        userService.updatePassword(token, newPassword);
        return ResponseEntity.noContent().build();
    }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }


    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



   /** @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }*/
}
