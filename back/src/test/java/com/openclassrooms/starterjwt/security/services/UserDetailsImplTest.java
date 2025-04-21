package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void testUserDetailsImplBuilder() {
        // Given
        Long id = 1L;
        String username = "test@test.com";
        String firstName = "Test";
        String lastName = "User";
        String password = "password";
        Boolean admin = true;

        // When
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .admin(admin)
                .build();

        // Then
        assertAll(
                () -> assertEquals(id, userDetails.getId()),
                () -> assertEquals(username, userDetails.getUsername()),
                () -> assertEquals(firstName, userDetails.getFirstName()),
                () -> assertEquals(lastName, userDetails.getLastName()),
                () -> assertEquals(password, userDetails.getPassword()),
                () -> assertEquals(admin, userDetails.getAdmin()),
                () -> assertTrue(userDetails.isAccountNonExpired()),
                () -> assertTrue(userDetails.isAccountNonLocked()),
                () -> assertTrue(userDetails.isCredentialsNonExpired()),
                () -> assertTrue(userDetails.isEnabled())
        );
    }

    @Test
    void testGetAuthorities() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testEquals() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

        assertAll(
                () -> assertEquals(user1, user1),
                () -> assertEquals(user1, user2),
                () -> assertNotEquals(user1, user3),
                () -> assertNotEquals(user1, null),
                () -> assertNotEquals(user1, new Object())
        );
    }

}