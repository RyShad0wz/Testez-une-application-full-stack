package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeacherMapperImplTest {

    @Autowired
    private TeacherMapper teacherMapper;

    private TeacherDto createSampleTeacherDto() {
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private Teacher createSampleTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        return teacher;
    }

    @Test
    void shouldMapToEntity() {
        TeacherDto dto = createSampleTeacherDto();
        Teacher result = teacherMapper.toEntity(dto);

        assertThat(result)
                .isNotNull()
                .extracting(
                        Teacher::getId,
                        Teacher::getLastName,
                        Teacher::getFirstName
                )
                .containsExactly(
                        dto.getId(),
                        dto.getLastName(),
                        dto.getFirstName()
                );
    }

    @Test
    void shouldMapToDto() {
        Teacher entity = createSampleTeacher();
        TeacherDto result = teacherMapper.toDto(entity);

        assertThat(result)
                .isNotNull()
                .extracting(
                        TeacherDto::getId,
                        TeacherDto::getLastName,
                        TeacherDto::getFirstName
                )
                .containsExactly(
                        entity.getId(),
                        entity.getLastName(),
                        entity.getFirstName()
                );
    }

    @Test
    void shouldMapToEntityList() {
        List<TeacherDto> dtoList = Arrays.asList(
                createSampleTeacherDto(),
                createSampleTeacherDto()
        );

        List<Teacher> result = teacherMapper.toEntity(dtoList);

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting(Teacher::getId)
                .containsExactly(dtoList.get(0).getId(), dtoList.get(1).getId());
    }

    @Test
    void shouldMapToDtoList() {
        List<Teacher> entityList = Arrays.asList(
                createSampleTeacher(),
                createSampleTeacher()
        );

        List<TeacherDto> result = teacherMapper.toDto(entityList);

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting(TeacherDto::getId)
                .containsExactly(entityList.get(0).getId(), entityList.get(1).getId());
    }

    @Test
    void shouldHandleNullInput() {
        assertThat(teacherMapper.toEntity((TeacherDto) null)).isNull();
        assertThat(teacherMapper.toDto((Teacher) null)).isNull();
        assertThat(teacherMapper.toEntity((List<TeacherDto>) null)).isNull();
        assertThat(teacherMapper.toDto((List<Teacher>) null)).isNull();
    }

    @Test
    void shouldMapEmptyList() {
        assertThat(teacherMapper.toEntity(Collections.emptyList())).isEmpty();
        assertThat(teacherMapper.toDto(Collections.emptyList())).isEmpty();
    }
}