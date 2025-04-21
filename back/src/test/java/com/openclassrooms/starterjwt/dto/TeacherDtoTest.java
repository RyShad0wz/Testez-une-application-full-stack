package com.openclassrooms.starterjwt.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TeacherDtoTest {

    private static Validator validator;
    private final LocalDateTime currentDateTime = LocalDateTime.now();

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateTeacherDto() {
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setCreatedAt(currentDateTime);
        dto.setUpdatedAt(currentDateTime);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getCreatedAt()).isEqualTo(currentDateTime);
        assertThat(dto.getUpdatedAt()).isEqualTo(currentDateTime);
    }
}