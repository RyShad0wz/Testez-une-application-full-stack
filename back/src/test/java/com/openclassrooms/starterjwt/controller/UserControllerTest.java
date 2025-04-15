/* package com.openclassrooms.starterjwt.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User()
                .setId(1L)
                .setEmail("test@studio.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword("password")
                .setAdmin(false);
    }

    @Test
    void shouldGetUserById() {
        // Arrange
        when(userService.findById(1L)).thenReturn(mockUser);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(mockUser);
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        // Arrange
        when(userService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidId() {
        // Act
        ResponseEntity<?> response = userController.findById("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldDeleteUserWhenAuthorized() {
        // Arrange
        when(userService.findById(1L)).thenReturn(mockUser);
        setupSecurityContext("test@studio.com");

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    void shouldReturnUnauthorizedWhenDeletingOtherUser() {
        // Arrange
        when(userService.findById(1L)).thenReturn(mockUser);
        setupSecurityContext("other@studio.com");

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).delete(any());
    }

    private void setupSecurityContext(String email) {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                email, "password", null);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
} */