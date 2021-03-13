package com.codewell.server.service;

import com.codewell.server.dto.UserDto;
import com.codewell.server.persistence.entity.UserEntity;
import com.codewell.server.persistence.repository.UserCredentialsRepository;
import com.codewell.server.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl target;

    @Test
    void test_getAllUsers_success()
    {
        final UserEntity userEntity = this.createUserEntity();
        when(userRepository.selectAll()).thenReturn(Collections.singletonList(userEntity));

        List<UserDto> users = target.getAllUsers();
        users.forEach(user -> this.assertEqualDtoAndEntity(user, userEntity));
    }

    @Test
    void test_getAllUsers_noUsers()
    {
        when(userRepository.selectAll()).thenReturn(Collections.emptyList());

        List<UserDto> users = target.getAllUsers();
        assertEquals(users.size(), 0);
    }

    @Test
    void test_getUserById_success()
    {
        final UserEntity userEntity = this.createUserEntity();
        when(userRepository.selectByUserId(anyString())).thenReturn(userEntity);

        UserDto user = target.getUserById(UUID.randomUUID().toString());
        this.assertEqualDtoAndEntity(user, userEntity);
    }

    private void assertEqualDtoAndEntity(final UserDto userDto, final UserEntity userEntity)
    {
        assertEquals(userDto.getId(), userEntity.getId());
        assertEquals(userDto.getUserId(), userEntity.getUserId());
        assertEquals(userDto.getEmail(), userEntity.getEmail());
        assertEquals(userDto.getFirstName(), userEntity.getFirstName());
        assertEquals(userDto.getLastName(), userEntity.getLastName());
        assertEquals(userDto.getAge(), userEntity.getAge());
        assertEquals(userDto.getCity(), userEntity.getCity());
        assertEquals(userDto.getIsAdmin(), userEntity.getIsAdmin());
    }

    private UserEntity createUserEntity()
    {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEmail("test@gmail.com");
        userEntity.setFirstName("Test");
        userEntity.setLastName("Name");
        userEntity.setAge(8);
        userEntity.setCity("Test City");
        userEntity.setIsAdmin("true");
        return userEntity;
    }
}
