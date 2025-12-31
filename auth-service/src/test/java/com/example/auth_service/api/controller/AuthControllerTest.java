package com.example.auth_service.api.controller;

import com.example.auth_service.api.dto.AuthRequest;
import com.example.auth_service.api.dto.AuthResponse;
import com.example.auth_service.api.dto.RegisterRequest;
import com.example.auth_service.security.config.SecurityConfig;
import com.example.auth_service.security.jwt.JwtService;
import com.example.auth_service.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService service;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_success() throws Exception {
        RegisterRequest request = new RegisterRequest("test@mail.ru", "123456");

        AuthResponse response = new AuthResponse("token", null);

        when(service.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void login_success() throws Exception {
        AuthRequest request = new AuthRequest("test@mail.ru", "123456");
        AuthResponse response = new AuthResponse("token", null);
        when(service.login(any(AuthRequest.class))).thenReturn(response);
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
