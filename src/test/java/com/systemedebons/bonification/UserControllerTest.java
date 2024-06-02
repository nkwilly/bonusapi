package com.systemedebons.bonification;

import com.systemedebons.bonification.Controller.UserController;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.Optional;

import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetUser() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

    }



    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setId("1");
        when(userService.getUserById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }
    @Test
    void testCreateUser() throws Exception {

        User user = new User();
        user.setId("1");
        when(userService.saveUser(any(User.class))).thenReturn(user);


        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"John\",\"prenom\":\"Doe\",\"email\":\"john.doe@example.com\",\"motDePasse\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testForgotPassword() throws Exception {
        doNothing().when(userService).resetPassword(anyString());

        mockMvc.perform(post("/api/user/forgot-password")
                        .param("email", "test@example.com"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).resetPassword("test@example.com");
    }

    @Test
    public void testResetPassword() throws Exception {
        doNothing().when(userService).updatePassword(anyString(), anyString());

        mockMvc.perform(post("/api/user/reset-password")
                        .param("token", "valid-token")
                        .param("newPassword", "newPassword"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updatePassword("valid-token", "newPassword");
    }

}
