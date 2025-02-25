package com.systemedebons.bonification;

import com.systemedebons.bonification.Entity.Administrator;
import com.systemedebons.bonification.Repository.AdministratorRepository;
import com.systemedebons.bonification.Service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class AdminServiceTests {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private AdministratorRepository adminRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void TestGetAdministrator() {
        Administrator admin = new Administrator();
        when(adminRepository.findById("1")).thenReturn(Optional.of(admin));

        Optional<Administrator> foudAdmin = adminService.getAdministrator("1");
        assertTrue(foudAdmin.isPresent());
        assertEquals(admin.getAdministratorId(), foudAdmin.get().getAdministratorId());
    }

    @Test
    void  testSaveAdministrator() {
        Administrator admin = new Administrator();
        when(adminRepository.save(any(Administrator.class))).thenReturn(admin);

        Administrator savedAdmin = adminService.saveAdministrator(admin);
        assertNotNull(savedAdmin);
        verify(adminRepository, times(1)).save(any(Administrator.class));
    }



    @Test
    void  testDeleteAdministrator() {
        doNothing().when(adminRepository).deleteById("1");
        adminService.deleteAdministrator("1");
        verify(adminRepository, times(1)).deleteById("1");

    }

}
