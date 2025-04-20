package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testGettersAndSetters() {
        // Given
        SignupRequest request = new SignupRequest();
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password123";

        // When
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPassword(password);

        // Then
        assertAll(
                () -> assertEquals(email, request.getEmail()),
                () -> assertEquals(firstName, request.getFirstName()),
                () -> assertEquals(lastName, request.getLastName()),
                () -> assertEquals(password, request.getPassword())
        );
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        SignupRequest request1 = createRequest("test@example.com", "John", "Doe", "password123");
        SignupRequest request2 = createRequest("test@example.com", "John", "Doe", "password123");
        SignupRequest differentRequest = createRequest("other@example.com", "Jane", "Smith", "password456");

        // Then
        assertAll(
                () -> assertEquals(request1, request2),
                () -> assertEquals(request1.hashCode(), request2.hashCode()),
                () -> assertNotEquals(request1, differentRequest),
                () -> assertNotEquals(request1.hashCode(), differentRequest.hashCode()),
                () -> assertNotEquals(request1, null),
                () -> assertNotEquals(request1, new Object())
        );
    }

    @Test
    void testToString() {
        // Given
        SignupRequest request = createRequest("test@example.com", "John", "Doe", "password123");

        // When
        String toStringResult = request.toString();

        // Then
        assertAll(
                () -> assertTrue(toStringResult.contains("test@example.com")),
                () -> assertTrue(toStringResult.contains("John")),
                () -> assertTrue(toStringResult.contains("Doe")),
                () -> assertTrue(toStringResult.contains("password123"))
        );
    }

    @Test
    void testCanEqual() {
        // Given
        SignupRequest request1 = new SignupRequest();
        SignupRequest request2 = new SignupRequest();

        // Then
        assertTrue(request1.canEqual(request2));
        assertFalse(request1.canEqual(new Object()));
    }

    private SignupRequest createRequest(String email, String firstName, String lastName, String password) {
        SignupRequest request = new SignupRequest();
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPassword(password);
        return request;
    }
}