package com.openclassrooms.starterjwt.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");
        testUser.setLastName("Doe");
        testUser.setFirstName("John");
        testUser.setPassword("encodedPassword");
        testUser.setAdmin(false);
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@test.com",
                "John",
                "Doe",
                false,
                "password"
        );

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication))
                .thenReturn("jwtToken");
        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(testUser));

        // When
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertThat(jwtResponse.getToken()).isEqualTo("jwtToken");
        assertThat(jwtResponse.getUsername()).isEqualTo("test@test.com");
    }

    @Test
    void shouldFailAuthenticationWithInvalidCredentials() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@test.com");
        loginRequest.setPassword("wrong");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When/Then
        assertThatThrownBy(() -> authController.authenticateUser(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("new@test.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Smith");

        when(userRepository.existsByEmail("new@test.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        // When
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");
    }

    @Test
    void shouldFailRegistrationWithExistingEmail() {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("existing@test.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Smith");

        when(userRepository.existsByEmail("existing@test.com"))
                .thenReturn(true);

        // When
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertThat(messageResponse.getMessage()).isEqualTo("Error: Email is already taken!");
    }

    @Test
    void shouldReturnAdminStatusCorrectly() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@test.com");
        loginRequest.setPassword("admin");

        User adminUser = new User();
        adminUser.setAdmin(true);

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "admin@test.com",
                "Admin",
                "User",
                false,
                "admin"
        );

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication))
                .thenReturn("jwtToken");
        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(adminUser));

        // When
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Then
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertThat(jwtResponse.getAdmin()).isTrue();
    }
}
