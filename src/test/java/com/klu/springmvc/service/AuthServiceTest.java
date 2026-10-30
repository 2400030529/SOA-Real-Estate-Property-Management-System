
package com.klu.springmvc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.klu.springmvc.dto.LoginRequest;
import com.klu.springmvc.dto.LoginResponse;
import com.klu.springmvc.dto.RegisterRequest;
import com.klu.springmvc.entity.User;
import com.klu.springmvc.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Password@123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password@123");

        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");
    }

    @Test
    void testRegisterSuccess() {
        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("Password@123"))
                .thenReturn("encodedPassword");

        String result = authService.register(registerRequest);

        assertEquals("User registered successfully", result);
        verify(passwordEncoder).encode("Password@123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterDuplicateEmail() {
        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        String result = authService.register(registerRequest);

        assertEquals("Email already registered", result);
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void testRegisterStoresEncodedPassword() {
        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("Password@123"))
                .thenReturn("encodedPassword");

        authService.register(registerRequest);

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPassword().equals("encodedPassword")
                && savedUser.getRole().equals("USER")
        ));
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                "Password@123", "encodedPassword"))
                .thenReturn(true);
        when(jwtService.generateToken(any(
                org.springframework.security.core.userdetails.User.class)))
                .thenReturn("test-jwt-token");

        LoginResponse result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("test-jwt-token", result.getToken());
        verify(jwtService).generateToken(any(
                org.springframework.security.core.userdetails.User.class));
    }

    @Test
    void testLoginWithInvalidEmail() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class,
                () -> authService.login(loginRequest));

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testLoginWithIncorrectPassword() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                "Password@123", "encodedPassword"))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(loginRequest));

        verify(jwtService, never()).generateToken(any());
    }
}
