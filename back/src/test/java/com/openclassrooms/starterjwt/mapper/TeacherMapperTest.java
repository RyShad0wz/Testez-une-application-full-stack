package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void shouldMapToEntity() {
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        Teacher result = teacherMapper.toEntity(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getLastName()).isEqualTo(dto.getLastName());
        assertThat(result.getFirstName()).isEqualTo(dto.getFirstName());
    }

    @Test
    void shouldMapToDto() {
        Teacher entity = new Teacher();
        entity.setId(1L);
        entity.setLastName("Doe");
        entity.setFirstName("John");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        TeacherDto result = teacherMapper.toDto(entity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getLastName()).isEqualTo(entity.getLastName());
        assertThat(result.getFirstName()).isEqualTo(entity.getFirstName());
    }
}