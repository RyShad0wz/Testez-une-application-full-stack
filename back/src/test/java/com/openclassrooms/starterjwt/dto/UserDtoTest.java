package com.openclassrooms.starterjwt.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserDtoTest {

    private static Validator validator;
    private final LocalDateTime currentDateTime = LocalDateTime.now();

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateUserDto() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@test.com");
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setAdmin(false);
        dto.setPassword("password");
        dto.setCreatedAt(currentDateTime);
        dto.setUpdatedAt(currentDateTime);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo("test@test.com");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.isAdmin()).isFalse();
        assertThat(dto.getPassword()).isEqualTo("password");
        assertThat(dto.getCreatedAt()).isEqualTo(currentDateTime);
        assertThat(dto.getUpdatedAt()).isEqualTo(currentDateTime);
    }
}