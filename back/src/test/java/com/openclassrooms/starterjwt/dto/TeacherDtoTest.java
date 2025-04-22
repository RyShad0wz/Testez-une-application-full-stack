package com.openclassrooms.starterjwt.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TeacherDtoTest {

    private final LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
    private final LocalDateTime anotherDateTime = LocalDateTime.of(2023, 1, 2, 0, 0);

    @Test
    void testEqualsAndHashCode() {
        // Given
        TeacherDto teacher1 = new TeacherDto(
                1L,
                "Doe",
                "John",
                fixedDateTime,
                fixedDateTime
        );

        TeacherDto teacher2 = new TeacherDto(
                1L,
                "Doe",
                "John",
                fixedDateTime,
                fixedDateTime
        );

        TeacherDto differentTeacher = new TeacherDto(
                2L,
                "Smith",
                "Jane",
                anotherDateTime,
                anotherDateTime
        );

        // Then
        assertThat(teacher1)
                .isEqualTo(teacher2)
                .hasSameHashCodeAs(teacher2)
                .isNotEqualTo(differentTeacher);

        assertThat(teacher1.hashCode())
                .isNotEqualTo(differentTeacher.hashCode());
    }

    @Test
    void testCanEqual() {
        // Given
        TeacherDto teacher1 = new TeacherDto();
        TeacherDto teacher2 = new TeacherDto();

        // When/Then
        assertThat(teacher1.canEqual(teacher2)).isTrue();
        assertThat(teacher1.canEqual(new Object())).isFalse();
    }

    @Test
    void testToString() {
        // Given
        TeacherDto teacher = new TeacherDto(
                1L,
                "Doe",
                "John",
                fixedDateTime,
                fixedDateTime
        );

        // When
        String toString = teacher.toString();

        // Then
        assertThat(toString)
                .contains("Doe")
                .contains("John")
                .contains("2023-01-01T00:00");
    }

    @Test
    void testAllArgsConstructor() {
        // When
        TeacherDto teacher = new TeacherDto(
                1L,
                "Doe",
                "John",
                fixedDateTime,
                fixedDateTime
        );

        // Then
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getCreatedAt()).isEqualTo(fixedDateTime);
        assertThat(teacher.getUpdatedAt()).isEqualTo(fixedDateTime);
    }

    @Test
    void testNoArgsConstructor() {
        // When
        TeacherDto teacher = new TeacherDto();

        // Then
        assertThat(teacher).isNotNull();
    }

    @Test
    void testSettersAndGetters() {
        // Given
        TeacherDto teacher = new TeacherDto();

        // When
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
        teacher.setCreatedAt(fixedDateTime);
        teacher.setUpdatedAt(fixedDateTime);

        // Then
        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getCreatedAt()).isEqualTo(fixedDateTime);
        assertThat(teacher.getUpdatedAt()).isEqualTo(fixedDateTime);
    }

    @Test
    void testEqualsWithNull() {
        // Given
        TeacherDto teacher = new TeacherDto(
                1L,
                "Doe",
                "John",
                fixedDateTime,
                fixedDateTime
        );

        // Then
        assertThat(teacher.equals(null)).isFalse();
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        TeacherDto teacher = new TeacherDto();
        Object other = new Object();

        // Then
        assertThat(teacher.equals(other)).isFalse();
    }
}