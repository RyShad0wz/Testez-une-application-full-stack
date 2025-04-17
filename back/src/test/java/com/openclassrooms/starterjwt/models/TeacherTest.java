package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TeacherTest {

    @Test
    public void testTeacherBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testTeacherSetters() {
        Teacher teacher = new Teacher();
        LocalDateTime now = LocalDateTime.now();

        teacher.setId(2L);
        teacher.setLastName("Smith");
        teacher.setFirstName("Jane");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertThat(teacher.getId()).isEqualTo(2L);
        assertThat(teacher.getLastName()).isEqualTo("Smith");
        assertThat(teacher.getFirstName()).isEqualTo("Jane");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testTeacherEqualsAndHashCode() {
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(1L)
                .lastName("Different")
                .firstName("Name")
                .build();

        Teacher teacher3 = Teacher.builder()
                .id(3L)
                .lastName("Doe")
                .firstName("John")
                .build();

        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1).isNotEqualTo(teacher3);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
    }

    @Test
    public void testTeacherToString() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        String toString = teacher.toString();

        assertThat(toString).contains("Teacher");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("lastName=Doe");
        assertThat(toString).contains("firstName=John");
    }

    @Test
    public void testTeacherAccessorsChaining() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher()
                .setId(1L)
                .setLastName("Doe")
                .setFirstName("John")
                .setCreatedAt(now)
                .setUpdatedAt(now);

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }
}