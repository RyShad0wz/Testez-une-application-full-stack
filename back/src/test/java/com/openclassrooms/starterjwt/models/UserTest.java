package com.openclassrooms.starterjwt.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTest {

    private User baseUser;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        baseUser = createValidUser();
    }

    private User createValidUser() {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .createdAt(testDateTime)
                .updatedAt(testDateTime)
                .build();
    }

    private User createMinimalValidUser() {
        return User.builder()
                .id(1L)
                .email("minimal@test.com")
                .lastName("Min")
                .firstName("imal")
                .password("minpass")
                .build();
    }

    @Test
    void testToString() {
        String userString = baseUser.toString();
        assertThat(userString)
                .contains("id=1")
                .contains("email=test@test.com")
                .contains("lastName=Doe")
                .contains("firstName=John");
    }

    // Test constructeurs
    @Test
    void testAllArgsConstructor() {
        User user = new User(
                1L, "test@test.com", "Doe", "John",
                "password123", false, testDateTime, testDateTime);

        assertThat(user).usingRecursiveComparison().isEqualTo(baseUser);
    }

    // Test constructeur avec 5 args
    @Test
    void testRequiredArgsConstructor() {
        User user = new User(
                "email@test.com",
                "Smith",
                "Jane",
                "pass123",
                true);

        assertThat(user)
                .extracting(
                        User::getEmail,
                        User::getLastName,
                        User::getFirstName,
                        User::getPassword,
                        User::isAdmin
                )
                .containsExactly(
                        "email@test.com",
                        "Smith",
                        "Jane",
                        "pass123",
                        true
                );
    }

    // Test equals et hashCode
    @Test
    void testEquals() {
        User sameIdDifferentEmail = createValidUser()
                .setEmail("different@email.com");

        User differentId = createValidUser()
                .setId(2L);

        assertEquals(baseUser, sameIdDifferentEmail);
        assertNotEquals(baseUser, differentId);
        assertEquals(baseUser.hashCode(), sameIdDifferentEmail.hashCode());
    }

    // Test canEqual
    @Test
    void testCanEqual() {
        User otherUser = createMinimalValidUser();
        assertTrue(baseUser.canEqual(otherUser));
        assertFalse(baseUser.canEqual(new Object()));
    }

    // Test builder avec valeurs minimales
    @Test
    void testBuilderWithMinimalValues() {
        User minimalUser = createMinimalValidUser();

        assertThat(minimalUser)
                .extracting(
                        User::getId,
                        User::getEmail,
                        User::getLastName,
                        User::getFirstName,
                        User::getPassword
                )
                .containsExactly(
                        1L,
                        "minimal@test.com",
                        "Min",
                        "imal",
                        "minpass"
                );
    }

    // Test des contraintes non-null
    @Test
    void shouldThrowWhenRequiredFieldIsNull() {
        assertThrows(NullPointerException.class, () -> {
            User.builder()
                    .id(1L)
                    // Omission intentionnelle d'un champ requis
                    //.email("test@test.com") // manquant
                    .lastName("Doe")
                    .firstName("John")
                    .password("pass")
                    .build();
        });
    }
}