package com.openclassrooms.starterjwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindByEmail() {
        // Given
        User user = new User()
                .setEmail("test@studio.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword("password")
                .setAdmin(false);

        entityManager.persist(user);
        entityManager.flush();

        // When
        User found = userRepository.findByEmail(user.getEmail()).orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void shouldNotFindByEmailWhenNotExists() {
        // When
        User found = userRepository.findByEmail("nonexistent@email.com").orElse(null);

        // Then
        assertThat(found).isNull();
    }

    @Test
    public void shouldSaveUser() {
        // Given
        User user = new User()
                .setEmail("new@studio.com")
                .setLastName("New")
                .setFirstName("User")
                .setPassword("password")
                .setAdmin(false);

        // When
        User saved = userRepository.save(user);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(userRepository.findById(saved.getId())).isPresent();
    }

    @Test
    public void shouldDeleteUser() {
        // Given
        User user = new User()
                .setEmail("delete@studio.com")
                .setLastName("ToDelete")
                .setFirstName("User")
                .setPassword("password")
                .setAdmin(false);

        entityManager.persist(user);
        entityManager.flush();

        // When
        userRepository.deleteById(user.getId());

        // Then
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
