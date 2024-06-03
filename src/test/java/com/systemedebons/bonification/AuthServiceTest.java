package com.systemedebons.bonification;

import com.systemedebons.bonification.Auth.JwtUtil;
import com.systemedebons.bonification.Service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class AuthServiceTest {

@InjectMocks
    private AuthService authService;
@Mock
    private AuthenticationManager authenticationManager;
@Mock
    private UserDetailsService userDetailsService;

@Mock
    private JwtUtil jwtUtil;
@Mock
    private PasswordEncoder passwordEncoder;

@BeforeEach
    public void setUp() {
    MockitoAnnotations.openMocks(this);

}


    @Test
    void testLogin() {
        // Arrange
        String username = "testuser";
        String password = "password";
        String token = "testtoken";

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(any())).thenReturn(token);

        // Act
        String result = String.valueOf(authService.login(username, password));

        // Assert
        assertNotNull(result);
        assertEquals(token, result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken(any());
    }


    @Test
    public void testLoginSuccess() {
        String username = "user";
        String password = "password";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken(authentication)).thenReturn("jwt-token");

        String token = authService.login(username, password);
        assertNotNull(token);
        assertEquals("jwt-token", token);
    }


}
