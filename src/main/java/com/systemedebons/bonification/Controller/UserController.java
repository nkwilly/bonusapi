package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Service.UserService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "Utilisateur Management System", description = "Operations pertaining to user in User Management System")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {

        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }


    @PostMapping
    public ResponseEntity<User> createUser(@Valid  @RequestBody User user) {
        try {
            User  saveUser = userService.saveUser(user);
            return ResponseEntity.ok(saveUser);
        }catch (IllegalArgumentException e){

            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<User> forgotPassword(@RequestParam String email) {

        userService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<User> resetPassword(@RequestParam String token , @RequestParam String newPassword) {
    try{
        userService.updatePassword(token, newPassword);
        return ResponseEntity.noContent().build();
    }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }


    }



}
