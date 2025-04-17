package com.openclassrooms.starterjwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void shouldFindById() {
        // Given
        Teacher teacher = new Teacher()
                .setLastName("Doe")
                .setFirstName("John")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        entityManager.persist(teacher);
        entityManager.flush();

        // When
        Teacher found = teacherRepository.findById(teacher.getId()).orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(teacher.getId());
        assertThat(found.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void shouldNotFindByIdWhenNotExists() {
        // When
        Teacher found = teacherRepository.findById(999L).orElse(null);

        // Then
        assertThat(found).isNull();
    }

    @Test
    public void shouldSaveTeacher() {
        // Given
        Teacher teacher = new Teacher()
                .setLastName("New")
                .setFirstName("Teacher")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        // When
        Teacher saved = teacherRepository.save(teacher);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(teacherRepository.findById(saved.getId())).isPresent();
    }

    @Test
    public void shouldDeleteTeacher() {
        // Given
        Teacher teacher = new Teacher()
                .setLastName("ToDelete")
                .setFirstName("Teacher")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        entityManager.persist(teacher);
        entityManager.flush();

        // When
        teacherRepository.deleteById(teacher.getId());

        // Then
        assertThat(teacherRepository.findById(teacher.getId())).isEmpty();
    }

    @Test
    public void shouldFindAllTeachers() {
        // Given
        // D'abord, compter le nombre d'enseignants existants
        long initialCount = teacherRepository.count();

        Teacher teacher1 = new Teacher()
                .setLastName("Doe")
                .setFirstName("John")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        Teacher teacher2 = new Teacher()
                .setLastName("Smith")
                .setFirstName("Jane")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        entityManager.persist(teacher1);
        entityManager.persist(teacher2);
        entityManager.flush();

        // When
        List<Teacher> teachers = teacherRepository.findAll();

        // Then
        assertThat(teachers).hasSize((int) (initialCount + 2));
        assertThat(teachers).extracting(Teacher::getLastName)
                .contains(teacher1.getLastName(), teacher2.getLastName());
    }
}