package com.openclassrooms.starterjwt.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

class SessionDtoTest {

    private final Date fixedDate = new Date(1672531200000L); // 1er janvier 2023
    private final LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
    private final List<Long> users = Arrays.asList(1L, 2L);

    @Test
    void testEqualsAndHashCode() {
        // Given
        SessionDto session1 = new SessionDto(
                1L,
                "Session 1",
                fixedDate,
                1L,
                "Description",
                users,
                fixedDateTime,
                fixedDateTime
        );

        SessionDto session2 = new SessionDto(
                1L,
                "Session 1",
                fixedDate,
                1L,
                "Description",
                users,
                fixedDateTime,
                fixedDateTime
        );

        SessionDto differentSession = new SessionDto(
                2L,
                "Session 2",
                new Date(),
                2L,
                "Other Description",
                Arrays.asList(3L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Then
        assertThat(session1)
                .isEqualTo(session2)
                .hasSameHashCodeAs(session2)
                .isNotEqualTo(differentSession);

        assertThat(session1.hashCode())
                .isNotEqualTo(differentSession.hashCode());
    }

    @Test
    void testCanEqual() {
        // Given
        SessionDto session1 = new SessionDto();
        SessionDto session2 = new SessionDto();

        // When/Then
        assertThat(session1.canEqual(session2)).isTrue();
        assertThat(session1.canEqual(new Object())).isFalse();
    }

    @Test
    void testToString() {
        // Given
        SessionDto session = new SessionDto(
                1L,
                "Session 1",
                fixedDate,
                1L,
                "Description",
                users,
                fixedDateTime,
                fixedDateTime
        );

        // When
        String toString = session.toString();

        // Then
        assertThat(toString)
                .contains("Session 1")
                .contains("Description")
                .contains("teacher_id=1");
    }

    @Test
    void testAllArgsConstructor() {
        // When
        SessionDto session = new SessionDto(
                1L,
                "Session 1",
                fixedDate,
                1L,
                "Description",
                users,
                fixedDateTime,
                fixedDateTime
        );

        // Then
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Session 1");
        assertThat(session.getDate()).isEqualTo(fixedDate);
        assertThat(session.getTeacher_id()).isEqualTo(1L);
        assertThat(session.getDescription()).isEqualTo("Description");
        assertThat(session.getUsers()).isEqualTo(users);
        assertThat(session.getCreatedAt()).isEqualTo(fixedDateTime);
        assertThat(session.getUpdatedAt()).isEqualTo(fixedDateTime);
    }

    @Test
    void testNoArgsConstructor() {
        // When
        SessionDto session = new SessionDto();

        // Then
        assertThat(session).isNotNull();
    }

    @Test
    void testSettersAndGetters() {
        // Given
        SessionDto session = new SessionDto();

        // When
        session.setId(1L);
        session.setName("Session 1");
        session.setDate(fixedDate);
        session.setTeacher_id(1L);
        session.setDescription("Description");
        session.setUsers(users);
        session.setCreatedAt(fixedDateTime);
        session.setUpdatedAt(fixedDateTime);

        // Then
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Session 1");
        assertThat(session.getDate()).isEqualTo(fixedDate);
        assertThat(session.getTeacher_id()).isEqualTo(1L);
        assertThat(session.getDescription()).isEqualTo("Description");
        assertThat(session.getUsers()).isEqualTo(users);
        assertThat(session.getCreatedAt()).isEqualTo(fixedDateTime);
        assertThat(session.getUpdatedAt()).isEqualTo(fixedDateTime);
    }

    @Test
    void testEqualsWithNull() {
        // Given
        SessionDto session = new SessionDto(
                1L,
                "Session 1",
                fixedDate,
                1L,
                "Description",
                users,
                fixedDateTime,
                fixedDateTime
        );

        // Then
        assertThat(session.equals(null)).isFalse();
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        SessionDto session = new SessionDto();
        Object other = new Object();

        // Then
        assertThat(session.equals(other)).isFalse();
    }

    @Test
    void testEqualsWithDifferentFields() {
        // Given
        SessionDto session1 = new SessionDto(
                1L,
                "Session 1",
                fixedDate,
                1L,
                "Description",
                users,
                fixedDateTime,
                fixedDateTime
        );

        SessionDto session2 = new SessionDto(
                1L,
                "Session 2", // Nom différent
                fixedDate,
                1L,
                "Description",
                users,
                fixedDateTime,
                fixedDateTime
        );

        // Then
        assertThat(session1).isNotEqualTo(session2);
    }
}