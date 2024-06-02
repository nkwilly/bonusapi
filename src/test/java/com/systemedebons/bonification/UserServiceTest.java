package com.systemedebons.bonification;

import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.EmailService;
import com.systemedebons.bonification.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testSaveUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setMotDePasse("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendWelcomeEmail("test@example.com");
    }




    @Test
    void testGetUserById() {
        User user = new User();
        user.setId("1");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.getUserById("1");
        assertTrue(userOptional.isPresent());
        assertEquals(user.getId(), userOptional.get().getId());
    }

    @Test
    public void testSaveUser_PasswordNull() {
        User user = new User();
        user.setEmail("test@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("Mot de Passe ne peut pas être null ou  vide", exception.getMessage());
    }



    @Test
    public void testResetPassword_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        userService.resetPassword("test@example.com");

        verify(emailService, times(1)).sendPasswordResetEmail(eq("test@example.com"), anyString());
    }

    @Test
    public void testUpdatePassword_Success() {
        User user = new User();
        user.setResetToken("valid-token");
        user.setMotDePasse("oldPassword");

        when(userRepository.findByResetToken("valid-token")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.updatePassword("valid-token", "newPassword");

        assertEquals("encodedNewPassword", user.getMotDePasse());
        assertNull(user.getResetToken());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdatePassword_InvalidToken() {
        when(userRepository.findByResetToken("invalid-token")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updatePassword("invalid-token", "newPassword");
        });

        assertEquals("Token invalide ou expiré", exception.getMessage());
    }


    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById("1");
        userService.deleteUser("1");
        verify(userRepository, times(1)).deleteById("1");
    }




}
