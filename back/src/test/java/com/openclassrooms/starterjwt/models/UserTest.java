package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTest {

    @Test
    void shouldCreateUser() {
        // Arrange & Act
        User user = new User()
                .setId(1L)
                .setEmail("test@studio.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword("password")
                .setAdmin(false);

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("test@studio.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldCompareUsers() {
        // Arrange
        User user1 = new User().setId(1L).setEmail("test@studio.com");
        User user2 = new User().setId(1L).setEmail("test@studio.com");
        User user3 = new User().setId(2L).setEmail("other@studio.com");

        // Assert
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}