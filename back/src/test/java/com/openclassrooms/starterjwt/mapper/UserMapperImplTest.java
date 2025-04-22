package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperImplTest {

    @Autowired
    private UserMapper userMapper;

    private UserDto createSampleUserDto() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@test.com");
        dto.setLastName("Doe");
        dto.setFirstName("John");
        dto.setAdmin(false);
        dto.setPassword("password123");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }

    private User createSampleUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setAdmin(false);
        user.setPassword("password123");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    @Test
    void shouldMapToEntity() {
        // Given
        UserDto dto = createSampleUserDto();

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
        // Given
        User entity = createSampleUser();

        // When
        UserDto result = userMapper.toDto(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getEmail()).isEqualTo(entity.getEmail());
        assertThat(result.getLastName()).isEqualTo(entity.getLastName());
        assertThat(result.isAdmin()).isEqualTo(entity.isAdmin());
    }

    @Test
    void shouldMapToEntityList() {
        // Given
        List<UserDto> dtoList = Arrays.asList(
                createSampleUserDto(),
                createSampleUserDto()
        );

        // When
        List<User> result = userMapper.toEntity(dtoList);

        // Then
        assertThat(result)
                .isNotNull()
                .hasSameSizeAs(dtoList);

        // Vérification du premier élément
        User firstResult = result.get(0);
        UserDto firstDto = dtoList.get(0);
        assertThat(firstResult.getId()).isEqualTo(firstDto.getId());
        assertThat(firstResult.getEmail()).isEqualTo(firstDto.getEmail());
    }

    @Test
    void shouldMapToDtoList() {
        // Given
        List<User> entityList = Arrays.asList(
                createSampleUser(),
                createSampleUser()
        );

        // When
        List<UserDto> result = userMapper.toDto(entityList);

        // Then
        assertThat(result)
                .isNotNull()
                .hasSameSizeAs(entityList);

        // Vérification du premier élément
        UserDto firstResult = result.get(0);
        User firstEntity = entityList.get(0);
        assertThat(firstResult.getId()).isEqualTo(firstEntity.getId());
        assertThat(firstResult.getEmail()).isEqualTo(firstEntity.getEmail());
    }

    @Test
    void shouldHandleNullInput() {
        // Test des cas null
        assertThat(userMapper.toEntity((UserDto) null)).isNull();
        assertThat(userMapper.toDto((User) null)).isNull();
        assertThat(userMapper.toEntity((List<UserDto>) null)).isNull();
        assertThat(userMapper.toDto((List<User>) null)).isNull();
    }
}