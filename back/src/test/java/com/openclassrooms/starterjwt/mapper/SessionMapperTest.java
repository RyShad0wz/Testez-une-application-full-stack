package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);

        user = new User();
        user.setId(1L);

        session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setDate(new Date());
        session.setDescription("Description");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user));
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Test Session");
        sessionDto.setDate(session.getDate());
        sessionDto.setDescription("Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L));
        sessionDto.setCreatedAt(session.getCreatedAt());
        sessionDto.setUpdatedAt(session.getUpdatedAt());
    }

    @Test
    void shouldMapToEntity() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);

        Session result = sessionMapper.toEntity(sessionDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(sessionDto.getId());
        assertThat(result.getName()).isEqualTo(sessionDto.getName());
        assertThat(result.getTeacher().getId()).isEqualTo(sessionDto.getTeacher_id());
        assertThat(result.getUsers().get(0).getId()).isEqualTo(sessionDto.getUsers().get(0));
    }

    @Test
    void shouldMapToDto() {
        SessionDto result = sessionMapper.toDto(session);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(session.getId());
        assertThat(result.getName()).isEqualTo(session.getName());
        assertThat(result.getTeacher_id()).isEqualTo(session.getTeacher().getId());
        assertThat(result.getUsers().get(0)).isEqualTo(session.getUsers().get(0).getId());
    }

    @Test
    void shouldMapEntityListToDtoList() {
        List<Session> sessions = Arrays.asList(session);
        List<SessionDto> result = sessionMapper.toDto(sessions);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(session.getId());
    }

    @Test
    void shouldMapDtoListToEntityList() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);

        List<SessionDto> sessionDtos = Arrays.asList(sessionDto);
        List<Session> result = sessionMapper.toEntity(sessionDtos);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(sessionDto.getId());
    }
}