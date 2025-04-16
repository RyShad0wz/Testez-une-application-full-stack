package com.openclassrooms.starterjwt.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "test@studio.com")
    void shouldGetUser() throws Exception {
        // Arrange
        User user = new User()
                .setId(1L)
                .setEmail("test@studio.com")
                .setLastName("Doe")  // Add this
                .setFirstName("John") // Add this
                .setPassword("password"); // Add this
        given(userService.findById(1L)).willReturn(user);

        // Act & Assert
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@studio.com")
    void shouldDeleteUser() throws Exception {
        // Arrange
        User user = new User()
                .setId(1L)
                .setEmail("test@studio.com")
                .setLastName("Doe")  // Add this
                .setFirstName("John") // Add this
                .setPassword("password"); // Add this
        given(userService.findById(1L)).willReturn(user);

        // Act & Assert
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
    }
}