package com.klu.springmvc.controller;

import com.klu.springmvc.dto.LoginRequest;
import com.klu.springmvc.dto.RegisterRequest;
import com.klu.springmvc.service.AuthService;
import com.klu.springmvc.service.CustomUserDetailsService;
import com.klu.springmvc.service.JwtService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // 1. Test successful registration
    @Test
    void testRegisterSuccess() throws Exception {

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn("User registered successfully");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Test User",
                        "email": "test@example.com",
                        "password": "Test@123"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    // 2. Test duplicate email registration
    @Test
    void testRegisterDuplicateEmail() throws Exception {

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn("Email already registered");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Test User",
                        "email": "test@example.com",
                        "password": "Test@123"
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already registered"));
    }

    // 3. Test successful login
    @Test
    void testLoginSuccess() throws Exception {

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(null);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "test@example.com",
                        "password": "Test@123"
                    }
                    """))
                .andExpect(status().isOk());
    }

    // 4. Test authenticated user profile
 // 4. Test authenticated user profile
    @Test
    void testProfileSuccess() throws Exception {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null
                );

        mockMvc.perform(get("/auth/profile")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Authenticated user: test@example.com"
                ));
    }
}