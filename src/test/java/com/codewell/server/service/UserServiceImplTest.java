package com.codewell.server.service;

import com.codewell.server.dto.UserDto;
import com.codewell.server.persistence.entity.UserCredentialsEntity;
import com.codewell.server.persistence.entity.UserEntity;
import com.codewell.server.persistence.repository.UserCredentialsRepository;
import com.codewell.server.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest
{
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserCredentialsRepository userCredentialsRepository;
    @Spy
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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

    @Test
    void test_getUserById_noUser()
    {
        when(userRepository.selectByUserId(anyString())).thenReturn(null);
        assertNull(target.getUserById(UUID.randomUUID().toString()));
    }

    @Test
    void test_createUser_success()
    {
        final UserEntity userEntity = this.createUserEntity();
        doNothing().when(userRepository).insert(any());
        doNothing().when(userCredentialsRepository).insert(any());
        when(userRepository.selectByUserId(anyString())).thenReturn(userEntity);

        final UserDto newUser = this.createNewUser();
        final UserDto createdUser = target.createUser(newUser);

        assertNull(newUser.getId());
        assertNotNull(newUser.getUserId());
        assertEqualDtoAndEntity(createdUser, userEntity);
    }

    @Test
    void test_createUser_adminPermission_notSpecified()
    {
        final UserEntity userEntity = this.createUserEntity();
        doNothing().when(userRepository).insert(any());
        doNothing().when(userCredentialsRepository).insert(any());
        when(userRepository.selectByUserId(anyString())).thenReturn(userEntity);

        final UserDto newUser = this.createNewUser();
        newUser.setIsAdmin(null);

        target.createUser(newUser);
        assertEquals(newUser.getIsAdmin(), "false");
    }

    @Test
    void test_updateUser_success()
    {
        final UserEntity userEntity = this.createUserEntity();
        final UserDto updateDto = new UserDto();
        updateDto.setUserId(userEntity.getUserId());
        updateDto.setEmail("newemail@gmail.com");
        updateDto.setFirstName("New");
        updateDto.setLastName("Test");
        updateDto.setAge(10);
        updateDto.setCity("New City");

        when(userRepository.selectByUserId(anyString())).thenReturn(userEntity);
        when(userRepository.update(any())).thenReturn(userEntity);

        final UserDto updatedUser = target.updateUser(userEntity.getUserId(), updateDto);

        assertNotNull(updatedUser);
        assertEqualDtoAndEntity(updatedUser, userEntity);
        assertEquals(updatedUser.getEmail(), updateDto.getEmail());
        assertEquals(updatedUser.getFirstName(), updateDto.getFirstName());
        assertEquals(updatedUser.getLastName(), updateDto.getLastName());
        assertEquals(updatedUser.getAge(), updateDto.getAge());
        assertEquals(updatedUser.getCity(), updateDto.getCity());
    }

    @Test
    void test_updateUser_userNotFound()
    {
        final UserDto updateDto = this.createNewUser();
        when(userRepository.selectByUserId(anyString())).thenReturn(null);

        final String userId = UUID.randomUUID().toString();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.updateUser(userId, updateDto));
        assertEquals(exception.getMessage(), String.format("No record found for user id: %s", userId));
    }

    @Test
    void test_updateUsernameAndPassword_success()
    {
        final UserCredentialsEntity originalCredentials = new UserCredentialsEntity();
        originalCredentials.setUsername("username");
        originalCredentials.setPassword("password");
        final UserDto newCredentials = new UserDto();
        newCredentials.setUsername("newUsername");
        newCredentials.setPassword("newPassword");
        when(userCredentialsRepository.select(anyString())).thenReturn(originalCredentials);

        target.updateUsernameAndPassword(UUID.randomUUID().toString(), newCredentials);
        assertEquals(originalCredentials.getUsername(), newCredentials.getUsername());
        assertTrue(passwordEncoder.matches("newPassword", originalCredentials.getPassword()));
    }

    @Test
    void test_updateUsernameAndPassword_userNotFound()
    {
        final UserDto newCredentials = new UserDto();
        newCredentials.setUsername("newUsername");
        newCredentials.setPassword("newPassword");
        when(userCredentialsRepository.select(anyString())).thenReturn(null);

        final String userId = UUID.randomUUID().toString();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.updateUsernameAndPassword(userId, newCredentials));
        assertEquals(exception.getMessage(), String.format("No login credentials found for user id: %s", userId));
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
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEmail("test@gmail.com");
        userEntity.setFirstName("Test");
        userEntity.setLastName("Name");
        userEntity.setAge(8);
        userEntity.setCity("Test City");
        userEntity.setIsAdmin("true");
        return userEntity;
    }

    private UserDto createNewUser()
    {
        final UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("username");
        userDto.setPassword("password");
        userDto.setEmail("test@gmail.com");
        userDto.setFirstName("Test");
        userDto.setLastName("Name");
        userDto.setAge(8);
        userDto.setCity("Test City");

        return userDto;
    }
}
