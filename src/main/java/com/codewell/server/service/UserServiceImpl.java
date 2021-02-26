package com.codewell.server.service;

import com.codewell.server.dto.UserDto;
import com.codewell.server.persistence.entity.UserEntity;
import com.codewell.server.persistence.repository.UserRepository;
import com.codewell.server.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@Singleton
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String NOT_ADMIN = "false";

    @Inject
    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto getUserById(final String userId)
    {
        LOGGER.info("Fetching user data by userId: {}", userId);
        return this.mapToUserDto(userRepository.selectByUserId(userId));
    }

    @Override
    public UserDto getUserByUsername(final String username)
    {
        LOGGER.info("Fetching user data by username: {}", username);
        return this.mapToUserDto(userRepository.selectByUsername(username));
    }

    @Override
    public UserDto createUser(final UserDto newUser)
    {
        newUser.setId(null);
        final String userId = UUID.randomUUID().toString();
        newUser.setUserId(userId);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Assert.isTrue(UserUtil.hasValidEmail(newUser), "Email is not valid");
        Assert.isTrue(UserUtil.hasValidAge(newUser), "Age must be greater than 0");
        Assert.isTrue(UserUtil.hasValidAddress(newUser), "Address must only contain letters");

        if (StringUtils.isEmpty(newUser.getIsAdmin()))
        {
            newUser.setIsAdmin(NOT_ADMIN);
        }

        final OffsetDateTime now = OffsetDateTime.now();
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);

        final UserEntity newUserEntity = this.mapToUserEntity(newUser);
        LOGGER.info("Inserting new user into users table: {}", newUserEntity);
        userRepository.insert(newUserEntity);
        return this.getUserById(userId);
    }

    @Override
    public UserDto updateUser(final UserDto userDto)
    {
        Assert.isTrue(UserUtil.hasValidEmail(userDto), "Email is not valid");
        Assert.isTrue(UserUtil.hasValidAge(userDto), "Age must be greater than 0");
        Assert.isTrue(UserUtil.hasValidAddress(userDto), "Address must only contain letters");

        final UserEntity original = userRepository.selectByUserId(userDto.getUserId());
        if (original == null)
        {
            throw new IllegalArgumentException(String.format("No record found for user id: %s", userDto.getUserId()));
        }
        original.setEmail(userDto.getEmail());
        original.setFirstName(userDto.getFirstName());
        original.setLastName(userDto.getLastName());
        original.setAge(userDto.getAge());
        original.setCity(userDto.getCity());
        original.setUpdatedAt(OffsetDateTime.now());
        userRepository.update(original);
        return this.mapToUserDto(original);
    }

    private UserEntity mapToUserEntity(final UserDto userDto)
    {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(userDto.getId());
        userEntity.setUserId(userDto.getUserId());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setAge(userDto.getAge());
        userEntity.setCity(userDto.getCity());
        userEntity.setIsAdmin(userDto.getIsAdmin());
        userEntity.setCreatedAt(userDto.getCreatedAt());
        userEntity.setUpdatedAt(userDto.getUpdatedAt());
        return userEntity;
    }

    private UserDto mapToUserDto(final UserEntity userEntity)
    {
        return Optional.ofNullable(userEntity)
            .map(entity -> UserDto.newBuilder()
                .id(userEntity.getId())
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .age(userEntity.getAge())
                .city(userEntity.getCity())
                .isAdmin(userEntity.getIsAdmin())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build())
            .orElse(null);
    }
}
