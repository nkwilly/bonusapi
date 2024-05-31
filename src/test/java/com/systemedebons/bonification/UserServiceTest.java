package com.systemedebons.bonification;

import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById("1");
        userService.deleteUser("1");
        verify(userRepository, times(1)).deleteById("1");
    }


    @Test
    void testSaveUser(){
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User userSaved = userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }


}
