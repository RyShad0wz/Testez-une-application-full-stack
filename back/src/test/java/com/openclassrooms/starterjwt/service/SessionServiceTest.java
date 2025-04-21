package com.openclassrooms.starterjwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;

import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User()
                .setId(1L)
                .setEmail("test@test.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword("password")
                .setAdmin(false);

        session = new Session()
                .setId(1L)
                .setName("Session 1")
                .setDate(new Date())
                .setDescription("Description")
                .setUsers(new ArrayList<>())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldCreateSession() {
        // Given
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // When
        Session createdSession = sessionService.create(session);

        // Then
        assertThat(createdSession).isEqualTo(session);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldDeleteSession() {
        // Given
        doNothing().when(sessionRepository).deleteById(anyLong());

        // When
        sessionService.delete(1L);

        // Then
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldFindAllSessions() {
        // Given
        List<Session> sessions = Arrays.asList(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        // When
        List<Session> result = sessionService.findAll();

        // Then
        assertThat(result).hasSize(1).contains(session);
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void shouldGetSessionById() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // When
        Session result = sessionService.getById(1L);

        // Then
        assertThat(result).isEqualTo(session);
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNullWhenSessionNotFound() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Session result = sessionService.getById(1L);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldUpdateSession() {
        // Given
        Session updatedSession = new Session()
                .setName("Updated Session")
                .setDescription("Updated Description");

        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Session result = sessionService.update(1L, updatedSession);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Updated Session");
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void shouldParticipateToSession() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // When
        sessionService.participate(1L, 1L);

        // Then
        assertThat(session.getUsers()).hasSize(1).contains(user);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldThrowNotFoundWhenParticipateWithInvalidIds() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void shouldThrowBadRequestWhenAlreadyParticipate() {
        // Given
        session.getUsers().add(user);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // When / Then
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void shouldNoLongerParticipate() {
        // Given
        session.getUsers().add(user);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // When
        sessionService.noLongerParticipate(1L, 1L);

        // Then
        assertThat(session.getUsers()).isEmpty();
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldThrowNotFoundWhenNoLongerParticipateWithInvalidSession() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When / Then
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void shouldThrowBadRequestWhenNotParticipating() {
        // Given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // When / Then
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }
}
