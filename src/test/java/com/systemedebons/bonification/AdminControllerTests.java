package com.systemedebons.bonification;

import com.systemedebons.bonification.Controller.AdminController;
import com.systemedebons.bonification.Entity.Administrator;
import com.systemedebons.bonification.Service.AdminService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminControllerTests {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }
    @Test
    void testGetAllAdmins() throws Exception {
        when(adminService.getAdmins()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/administrator"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetAdminById() throws Exception {
        Administrator admin = new Administrator();
        admin.setAdministratorId("1");
        when(adminService.getAdministrator("1")).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/api/administrator/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.administratorId").value("1"));
    }

    @Test
    void testCreateAdmin() throws Exception {
        Administrator admin = new Administrator();
        admin.setAdministratorId("1");
        when(adminService.createAdministrator(any(Administrator.class))).thenReturn(admin);


        mockMvc.perform(post("/api/administrator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"John\",\"prenom\":\"Doe\",\"username\":\"Doe\",\"email\":\"john.doe@example.com\",\"motDePasse\":\"password\"}"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.administratorId").value("1"));
    }

    @Test
    void testDeleteAdministrator() throws Exception {
        doNothing().when(adminService).deleteAdministrator("1");

        mockMvc.perform(delete("/api/administrator/delete/1"))
                .andExpect(status().isNoContent());
    }



}
