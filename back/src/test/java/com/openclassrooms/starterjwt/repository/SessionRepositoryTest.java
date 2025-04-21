package com.openclassrooms.starterjwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    private Teacher teacher;
    private User user;

    @BeforeEach
    public void setUp() {
        teacher = new Teacher()
                .setLastName("Doe")
                .setFirstName("John");
        entityManager.persist(teacher);

        user = new User()
                .setEmail("user@test.com")
                .setLastName("User")
                .setFirstName("Test")
                .setPassword("password")
                .setAdmin(false);
        entityManager.persist(user);
    }

    private Session createValidSession() {
        return new Session()
                .setName("Test Session")
                .setDate(new Date())
                .setDescription("Valid Description") // Description non nulle
                .setTeacher(teacher)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void shouldSaveAndFindById() {
        // Given
        Session session = createValidSession();
        Session savedSession = entityManager.persist(session);
        entityManager.flush();

        // When
        Optional<Session> foundSession = sessionRepository.findById(savedSession.getId());

        // Then
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().getName()).isEqualTo(session.getName());
        assertThat(foundSession.get().getTeacher()).isEqualTo(teacher);
    }

    @Test
    public void shouldSaveSessionWithUsers() {
        // Given
        Session session = createValidSession();
        session.setUsers(new ArrayList<>()); // Initialisation explicite de la liste
        session.getUsers().add(user);

        Session savedSession = entityManager.persist(session);
        entityManager.flush();

        // Détacher et recharger pour s'assurer que tout est bien persisté
        entityManager.detach(savedSession);
        Session foundSession = sessionRepository.findById(savedSession.getId())
                .orElseThrow(() -> new AssertionError("Session should be found"));

        // Then
        assertThat(foundSession.getUsers())
                .isNotEmpty()
                .hasSize(1)
                .extracting(User::getEmail)
                .containsExactly(user.getEmail());
    }

    @Test
    public void shouldUpdateSession() {
        // Given
        Session session = createValidSession();
        Session savedSession = entityManager.persist(session);
        entityManager.flush();

        // When
        savedSession.setName("Updated Name");
        sessionRepository.save(savedSession);
        entityManager.flush();
        entityManager.clear();

        Session updatedSession = sessionRepository.findById(savedSession.getId()).orElse(null);

        // Then
        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getName()).isEqualTo("Updated Name");
    }

    @Test
    public void shouldDeleteSession() {
        // Given
        Session session = createValidSession();
        Session savedSession = entityManager.persist(session);
        entityManager.flush();

        // When
        sessionRepository.deleteById(savedSession.getId());
        entityManager.flush();

        // Then
        assertThat(sessionRepository.findById(savedSession.getId())).isEmpty();
    }

    @Test
    public void shouldFindAllSessions() {
        // Given
        Session session1 = createValidSession();
        Session session2 = createValidSession().setName("Another Session");

        entityManager.persist(session1);
        entityManager.persist(session2);
        entityManager.flush();

        // When
        List<Session> sessions = sessionRepository.findAll();

        // Then
        assertThat(sessions)
                .hasSize(2)
                .extracting(Session::getName)
                .containsExactlyInAnyOrder("Test Session", "Another Session");
    }
}