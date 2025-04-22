package com.openclassrooms.starterjwt.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class UserDtoTest {

    private final LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);

    @Test
    void testEqualsAndHashCode() {
        // Given
        UserDto user1 = new UserDto(
                1L,
                "test@test.com",
                "Doe",
                "John",
                false,
                "password",
                fixedDateTime,
                fixedDateTime
        );

        UserDto user2 = new UserDto(
                1L,
                "test@test.com",
                "Doe",
                "John",
                false,
                "password",
                fixedDateTime,
                fixedDateTime
        );

        UserDto differentUser = new UserDto(
                2L,
                "other@test.com",
                "Smith",
                "Jane",
                true,
                "password",
                fixedDateTime,
                fixedDateTime
        );

        // Then
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).hasSameHashCodeAs(user2);
        assertThat(user1).isNotEqualTo(differentUser);
        assertThat(user1.hashCode()).isNotEqualTo(differentUser.hashCode());
    }

    @Test
    void testCanEqual() {
        // Given
        UserDto user1 = new UserDto();
        UserDto user2 = new UserDto();

        // When/Then
        assertThat(user1.canEqual(user2)).isTrue();
        assertThat(user1.canEqual(new Object())).isFalse();
    }

    @Test
    void testToString() {
        // Given
        UserDto user = new UserDto(
                1L,
                "test@test.com",
                "Doe",
                "John",
                false,
                null,
                fixedDateTime,
                fixedDateTime
        );

        // When
        String toString = user.toString();

        // Then
        assertThat(toString).contains("test@test.com");
        assertThat(toString).contains("Doe");
        assertThat(toString).contains("John");
    }

    @Test
    void testAllSetters() {
        // Given
        UserDto user = new UserDto();

        // When
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setAdmin(false);
        user.setPassword("password");
        user.setCreatedAt(fixedDateTime);
        user.setUpdatedAt(fixedDateTime);

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.isAdmin()).isFalse();
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getCreatedAt()).isEqualTo(fixedDateTime);
        assertThat(user.getUpdatedAt()).isEqualTo(fixedDateTime);
    }

    @Test
    void testConstructor() {
        // When
        UserDto user = new UserDto(
                1L,
                "test@test.com",
                "Doe",
                "John",
                false,
                "password",
                fixedDateTime,
                fixedDateTime
        );

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.isAdmin()).isFalse();
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getCreatedAt()).isEqualTo(fixedDateTime);
        assertThat(user.getUpdatedAt()).isEqualTo(fixedDateTime);
    }
}