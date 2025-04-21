package com.openclassrooms.starterjwt.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SessionDtoTest {

    private static Validator validator;
    private final Date currentDate = new Date();
    private final LocalDateTime currentDateTime = LocalDateTime.now();

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateSessionDto() {
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Test Session");
        dto.setDate(currentDate);
        dto.setTeacher_id(1L);
        dto.setDescription("Description");
        dto.setUsers(Arrays.asList(1L, 2L));
        dto.setCreatedAt(currentDateTime);
        dto.setUpdatedAt(currentDateTime);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test Session");
        assertThat(dto.getDate()).isEqualTo(currentDate);
        assertThat(dto.getTeacher_id()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Description");
        assertThat(dto.getUsers()).containsExactly(1L, 2L);
        assertThat(dto.getCreatedAt()).isEqualTo(currentDateTime);
        assertThat(dto.getUpdatedAt()).isEqualTo(currentDateTime);
    }
}