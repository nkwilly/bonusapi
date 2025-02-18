package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.payload.request.LoginRequest;
import com.systemedebons.bonification.Service.UserService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "Utilisateur Management System", description = "Operations pertaining to user in User Management System")
@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "Endpoints for managing users")
public class UserController {

    private UserService userService;

    @Operation(summary = "Retrieve all users", description = "Returns a list of all users, only accessible by ADMIN.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of users")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {return userService.getAllUsers();}

    @Operation(summary = "Retrieve a user by ID", description = "Fetches a user based on their unique ID, only accessible by ADMIN.")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user", description = "Saves a new user to the database.")
    @ApiResponse(responseCode = "200", description = "User successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @PostMapping
    public ResponseEntity<User> createUser(@Valid  @RequestBody User user) {
        try {
            User  saveUser = userService.saveUser(user);
            return ResponseEntity.ok(saveUser);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Delete a user", description = "Deletes a user using their ID, only accessible by ADMIN.")
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Initiate password reset", description = "Sends a password reset email to the user.")
    @ApiResponse(responseCode = "204", description = "Password reset initiated")
    @ApiResponse(responseCode = "400", description = "Invalid email")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        try {
            userService.resetPassword(email);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Reset password", description = "Resets a user's password using a token.")
    @ApiResponse(responseCode = "204", description = "Password successfully reset")
    @ApiResponse(responseCode = "400", description = "Invalid token or password")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token , @RequestParam String newPassword) {
        try{
            userService.updatePassword(token, newPassword);
            return ResponseEntity.noContent().build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update a user", description = "Updates an existing user's details, only accessible by ADMIN.")
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
