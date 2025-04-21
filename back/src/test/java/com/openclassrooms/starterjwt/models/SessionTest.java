package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SessionTest {

    @Test
    void shouldCreateSession() {
        // Arrange
        Long id = 1L;
        String name = "Test Session";
        Date date = new Date();
        String description = "Test Description";
        Teacher teacher = new Teacher().setId(1L);
        List<User> users = new ArrayList<>();
        users.add(new User().setId(1L));
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        Session session = new Session()
                .setId(id)
                .setName(name)
                .setDate(date)
                .setDescription(description)
                .setTeacher(teacher)
                .setUsers(users)
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, session.getId());
        assertEquals(name, session.getName());
        assertEquals(date, session.getDate());
        assertEquals(description, session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(createdAt, session.getCreatedAt());
        assertEquals(updatedAt, session.getUpdatedAt());
    }

    @Test
    void shouldCompareSessions() {
        // Arrange
        Session session1 = new Session()
                .setId(1L)
                .setName("Session 1")
                .setDate(new Date(1234567890L))
                .setDescription("Description 1");

        Session session2 = new Session()
                .setId(1L)
                .setName("Session 1")
                .setDate(new Date(1234567890L))
                .setDescription("Description 1");

        Session session3 = new Session()
                .setId(2L)
                .setName("Session 2")
                .setDate(new Date(9876543210L))
                .setDescription("Description 2");

        // Assert
        assertEquals(session1, session2);
        assertNotEquals(session1, session3);
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    void shouldCheckToString() {
        // Arrange
        Session session = new Session()
                .setId(1L)
                .setName("Test Session")
                .setDate(new Date())
                .setDescription("Test Description");

        // Act
        String sessionString = session.toString();

        // Assert
        assertNotNull(sessionString);
        assertTrue(sessionString.contains("Session"));
        assertTrue(sessionString.contains("id=1"));
        assertTrue(sessionString.contains("name=Test Session"));
    }
}
