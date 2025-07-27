package com.translation.managment.service.controller;

import com.translation.managment.service.dto.JwtResponseDTO;
import com.translation.managment.service.dto.LoginRequestDTO;
import com.translation.managment.service.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.mockito.Mockito.*;

class LogInControllerTest {
    @Mock
    AuthenticationManager authManager;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    UserDetailsService userDetailsService;
    @InjectMocks
    LogInController logInController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_WhenValidCredentials_ShouldReturnJwtToken() {
        // Arrange
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user", "pass");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user");
        when(jwtUtil.generateToken("user")).thenReturn("mock-jwt-token");

        // Act
        ResponseEntity<?> response = logInController.login(loginRequestDTO);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertInstanceOf(JwtResponseDTO.class, response.getBody());
        JwtResponseDTO jwtResponse = (JwtResponseDTO) response.getBody();
        Assertions.assertEquals("mock-jwt-token", jwtResponse.getToken());
    }

    @Test
    void testLogin_WhenUsernameOrPasswordMissing_ShouldReturnBadRequest() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(null, null);

        ResponseEntity<?> response = logInController.login(loginRequestDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Username or password is missing", response.getBody());
    }

    @Test
    void testLogin_WhenAuthenticationFails_ShouldReturnUnauthorized() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUserName("user");
        loginRequestDTO.setPassword("wrongPassword");

        when(authManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = logInController.login(loginRequestDTO);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Invalid credentials", response.getBody());
    }

}