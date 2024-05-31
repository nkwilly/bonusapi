package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Service.UserService;
import io.swagger.annotations.Api;
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
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }




}
