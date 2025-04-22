package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String jwtSecret = "testSecret";
    private final int jwtExpirationMs = 86400000;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Injection des valeurs avec ReflectionTestUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void validateJwtToken_InvalidSignature_ShouldReturnFalse() {
        // Création d'un token avec une mauvaise signature
        String invalidSignatureToken = Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret") // Mauvais secret
                .compact();

        assertFalse(jwtUtils.validateJwtToken(invalidSignatureToken));
    }

    @Test
    void validateJwtToken_UnsupportedToken_ShouldReturnFalse() {
        // Création d'un token avec un format non supporté
        String unsupportedToken = "header.not-a-real-jwt.signature";

        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }

    @Test
    void generateJwtToken_ShouldReturnValidToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("test@test.com")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, Collections.emptyList());

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertEquals("test@test.com", jwtUtils.getUserNameFromJwtToken(token));
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_InvalidToken_ShouldReturnFalse() {
        assertFalse(jwtUtils.validateJwtToken("invalidToken"));
    }

    @Test
    void validateJwtToken_ExpiredToken_ShouldReturnFalse() {
        // Création d'un token expiré
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_MalformedToken_ShouldReturnFalse() {
        // Token malformé (manque une partie)
        assertFalse(jwtUtils.validateJwtToken("part1.part2"));
    }

    @Test
    void validateJwtToken_EmptyClaims_ShouldReturnFalse() {
        // Token vide
        assertFalse(jwtUtils.validateJwtToken(""));
    }

    @Test
    void getUserNameFromJwtToken_ShouldReturnUsername() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Act
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals("test@test.com", username);
    }

    @Test
    void getUserNameFromJwtToken_InvalidToken_ShouldThrow() {
        assertThrows(JwtException.class, () -> {
            jwtUtils.getUserNameFromJwtToken("invalidToken");
        });
    }

    @Test
    void getUserNameFromJwtToken_ExpiredToken_ShouldThrow() {
        // Création d'un token expiré
        String token = Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtils.getUserNameFromJwtToken(token);
        });
    }
}