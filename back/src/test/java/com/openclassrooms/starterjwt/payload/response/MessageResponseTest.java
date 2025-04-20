package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void givenMessage_whenConstructor_thenCreateCorrectObject() {
        // Given
        String message = "Test message";

        // When
        MessageResponse response = new MessageResponse(message);

        // Then
        assertEquals(message, response.getMessage());
    }

    @Test
    void givenSetter_whenModifyMessage_thenValueUpdated() {
        // Given
        MessageResponse response = new MessageResponse("Initial message");

        // When
        response.setMessage("Updated message");

        // Then
        assertEquals("Updated message", response.getMessage());
    }

    @Test
    void givenNullMessage_whenConstructor_thenCreateObject() {
        // When
        MessageResponse response = new MessageResponse(null);

        // Then
        assertNull(response.getMessage());
    }

    @Test
    void givenNullMessage_whenSetter_thenValueUpdated() {
        // Given
        MessageResponse response = new MessageResponse("Initial");

        // When
        response.setMessage(null);

        // Then
        assertNull(response.getMessage());
    }
}