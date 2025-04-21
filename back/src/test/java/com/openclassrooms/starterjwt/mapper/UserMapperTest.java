package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapToEntity() {
        // Given
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@test.com");
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setAdmin(false);
        dto.setPassword("password123"); // Ajout du mot de passe non null
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        // When
        User result = userMapper.toEntity(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        assertThat(result.getLastName()).isEqualTo(dto.getLastName());
        assertThat(result.isAdmin()).isEqualTo(dto.isAdmin());
        assertThat(result.getPassword()).isEqualTo(dto.getPassword());
    }

    @Test
    void shouldMapToDto() {
        User entity = new User();
        entity.setId(1L);
        entity.setEmail("test@test.com");
        entity.setLastName("Doe");
        entity.setFirstName("John");
        entity.setAdmin(false);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        UserDto result = userMapper.toDto(entity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getEmail()).isEqualTo(entity.getEmail());
        assertThat(result.getLastName()).isEqualTo(entity.getLastName());
        assertThat(result.isAdmin()).isEqualTo(entity.isAdmin());
    }
}