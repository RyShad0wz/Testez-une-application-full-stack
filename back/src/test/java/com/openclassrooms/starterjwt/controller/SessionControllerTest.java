package com.openclassrooms.starterjwt.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session mockSession;
    private SessionDto mockSessionDto;

    @BeforeEach
    void setUp() {
        mockSession = new Session()
                .setId(1L)
                .setName("Test Session")
                .setDate(new Date())
                .setDescription("Test Description")
                .setTeacher(null)
                .setUsers(new ArrayList<>())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        mockSessionDto = new SessionDto();
        mockSessionDto.setId(1L);
        mockSessionDto.setName("Test Session");
    }

    @Test
    void shouldFindSessionById() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        // Act
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockSessionDto, response.getBody());
        verify(sessionService, times(1)).getById(1L);
        verify(sessionMapper, times(1)).toDto(mockSession);
    }

    @Test
    void shouldReturnNotFoundWhenSessionDoesNotExist() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidIdFormat() {
        // Act
        ResponseEntity<?> response = sessionController.findById("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldFindAllSessions() {
        // Utilisation de Arrays.asList() au lieu de List.of()
        List<Session> sessions = Arrays.asList(mockSession);
        List<SessionDto> sessionDtos = Arrays.asList(mockSessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());
    }

    @Test
    void shouldCreateSession() {
        // Arrange
        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionService.create(mockSession)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        // Act
        ResponseEntity<?> response = sessionController.create(mockSessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockSessionDto, response.getBody());
    }

    @Test
    void shouldUpdateSession() {
        // Arrange
        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionService.update(1L, mockSession)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        // Act
        ResponseEntity<?> response = sessionController.update("1", mockSessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockSessionDto, response.getBody());
    }

    @Test
    void shouldDeleteSession() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(mockSession);
        doNothing().when(sessionService).delete(1L);

        // Act
        ResponseEntity<?> response = sessionController.save("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    void shouldParticipateToSession() {
        // Arrange
        doNothing().when(sessionService).participate(1L, 1L);

        // Act
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).participate(1L, 1L);
    }

    @Test
    void shouldNoLongerParticipate() {
        // Arrange
        doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).noLongerParticipate(1L, 1L);
    }

    @Test
    void shouldHandleNumberFormatException() {
        // Test for all methods that take ID parameters
        ResponseEntity<?> findByIdResponse = sessionController.findById("invalid");
        ResponseEntity<?> updateResponse = sessionController.update("invalid", mockSessionDto);
        ResponseEntity<?> deleteResponse = sessionController.save("invalid");
        ResponseEntity<?> participateResponse = sessionController.participate("invalid", "1");
        ResponseEntity<?> noLongerParticipateResponse = sessionController.noLongerParticipate("invalid", "1");

        assertEquals(HttpStatus.BAD_REQUEST, findByIdResponse.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, deleteResponse.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, participateResponse.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, noLongerParticipateResponse.getStatusCode());
    }
}
