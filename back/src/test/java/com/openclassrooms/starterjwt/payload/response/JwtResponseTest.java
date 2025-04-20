package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void givenValidData_whenConstructor_thenCreateCorrectObject() {
        // Given
        String token = "testToken";
        Long id = 1L;
        String username = "test@test.com";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;

        // When
        JwtResponse response = new JwtResponse(token, id, username, firstName, lastName, admin);

        // Then
        assertAll(
                () -> assertEquals(token, response.getToken()),
                () -> assertEquals("Bearer", response.getType()),
                () -> assertEquals(id, response.getId()),
                () -> assertEquals(username, response.getUsername()),
                () -> assertEquals(firstName, response.getFirstName()),
                () -> assertEquals(lastName, response.getLastName()),
                () -> assertEquals(admin, response.getAdmin())
        );
    }

    @Test
    void givenSetters_whenModifyFields_thenValuesUpdated() {
        // Given
        JwtResponse response = new JwtResponse("token", 1L, "user", "John", "Doe", false);

        // When
        response.setToken("newToken");
        response.setType("NewType");
        response.setId(2L);
        response.setUsername("new@user.com");
        response.setFirstName("Jane");
        response.setLastName("Smith");
        response.setAdmin(true);

        // Then
        assertAll(
                () -> assertEquals("newToken", response.getToken()),
                () -> assertEquals("NewType", response.getType()),
                () -> assertEquals(2L, response.getId()),
                () -> assertEquals("new@user.com", response.getUsername()),
                () -> assertEquals("Jane", response.getFirstName()),
                () -> assertEquals("Smith", response.getLastName()),
                () -> assertTrue(response.getAdmin())
        );
    }
}