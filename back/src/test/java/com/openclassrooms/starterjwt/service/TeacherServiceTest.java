package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void shouldFindAllTeachers() {
        // Given
        Teacher teacher1 = new Teacher()
                .setId(1L)
                .setLastName("Doe")
                .setFirstName("John");

        Teacher teacher2 = new Teacher()
                .setId(2L)
                .setLastName("Smith")
                .setFirstName("Jane");

        List<Teacher> expectedTeachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        // When
        List<Teacher> actualTeachers = teacherService.findAll();

        // Then
        assertThat(actualTeachers).isEqualTo(expectedTeachers);
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void shouldFindTeacherById() {
        // Given
        Long teacherId = 1L;
        Teacher expectedTeacher = new Teacher()
                .setId(teacherId)
                .setLastName("Doe")
                .setFirstName("John");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(expectedTeacher));

        // When
        Teacher actualTeacher = teacherService.findById(teacherId);

        // Then
        assertThat(actualTeacher).isEqualTo(expectedTeacher);
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void shouldReturnNullWhenTeacherNotFound() {
        // Given
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When
        Teacher actualTeacher = teacherService.findById(teacherId);

        // Then
        assertThat(actualTeacher).isNull();
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void shouldReturnNullImmediatelyWhenIdIsNull() {
        // When
        Teacher result = teacherService.findById(null);

        // Then
        assertThat(result).isNull();

        // Vérification cruciale - le repository ne doit jamais être appelé
        verify(teacherRepository, never()).findById(any());
    }
}