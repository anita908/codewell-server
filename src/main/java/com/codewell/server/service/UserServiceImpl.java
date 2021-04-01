package com.codewell.server.service;

import com.codewell.server.dto.UserCredentialsDto;
import com.codewell.server.dto.UserDto;
import com.codewell.server.persistence.entity.UserEntity;
import com.codewell.server.persistence.repository.UserRepository;
import com.codewell.server.util.DataValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Named
@Singleton
public class UserServiceImpl implements UserService
{
    private final AuthService authService;
    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String NOT_ADMIN = "false";

    @Inject
    public UserServiceImpl(final AuthService authService,
                           final UserRepository userRepository)
    {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers()
    {
        LOGGER.info("Getting data for all users");
        return userRepository.selectAll()
            .stream()
            .map(this::mapToUserDto)
            .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(final String userId)
    {
        LOGGER.info("Fetching user data by userId: {}", userId);
        return this.mapToUserDto(userRepository.selectByUserId(userId));
    }

    @Override
    public UserDto createUser(final UserDto newUser)
    {
        newUser.setId(null);
        final String userId = UUID.randomUUID().toString();
        newUser.setUserId(userId);

        Assert.isTrue(DataValidator.isValidUsername(newUser.getUsername()), "Username is not valid");
        Assert.isTrue(DataValidator.isValidEmail(newUser.getEmail()), "Email is not valid");
        Assert.isTrue(DataValidator.isValidAge(newUser.getAge()), "Age must be greater than 0");
        Assert.isTrue(DataValidator.isValidAddress(newUser.getCity()), "Address must only contain letters");

        if (StringUtils.isEmpty(newUser.getIsAdmin()))
        {
            newUser.setIsAdmin(NOT_ADMIN);
        }

        final UserEntity newUserEntity = this.mapToUserEntity(newUser);
        LOGGER.info("Inserting new user into users table: {}", newUserEntity);
        userRepository.insert(newUserEntity);

        authService.createUsernameAndPassword(userId, new UserCredentialsDto(newUser.getUsername(), newUser.getPassword()));
        return this.getUserById(userId);
    }

    @Override
    public UserDto updateUser(final String userId, final UserDto userDto)
    {
        Assert.isTrue(DataValidator.isValidEmail(userDto.getEmail()), "Email is not valid");
        Assert.isTrue(DataValidator.isValidAge(userDto.getAge()), "Age must be greater than 0");
        Assert.isTrue(DataValidator.isValidAddress(userDto.getCity()), "Address must only contain letters");

        final UserEntity original = userRepository.selectByUserId(userId);
        if (original == null)
        {
            throw new IllegalArgumentException(String.format("No record found for user id: %s", userId));
        }

        original.setEmail(userDto.getEmail());
        original.setFirstName(userDto.getFirstName());
        original.setLastName(userDto.getLastName());
        original.setAge(userDto.getAge());
        original.setCity(userDto.getCity());
        original.setUpdatedAt(OffsetDateTime.now());

        return this.mapToUserDto(userRepository.update(original));
    }

    private UserEntity mapToUserEntity(final UserDto userDto)
    {
        if (userDto == null)
        {
            return null;
        }

        final UserEntity userEntity = new UserEntity();
        userEntity.setId(userDto.getId());
        userEntity.setUserId(userDto.getUserId());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setAge(userDto.getAge());
        userEntity.setCity(userDto.getCity());
        userEntity.setIsAdmin(userDto.getIsAdmin());
        final OffsetDateTime currentTime = OffsetDateTime.now();
        userEntity.setCreatedAt(currentTime);
        userEntity.setUpdatedAt(currentTime);
        return userEntity;
    }

    private UserDto mapToUserDto(final UserEntity userEntity)
    {
        return Optional.ofNullable(userEntity).map(entity -> UserDto.newBuilder()
            .id(userEntity.getId())
            .userId(userEntity.getUserId())
            .email(userEntity.getEmail())
            .firstName(userEntity.getFirstName())
            .lastName(userEntity.getLastName())
            .age(userEntity.getAge())
            .city(userEntity.getCity())
            .isAdmin(userEntity.getIsAdmin())
            .build())
            .orElse(null);
    }
}
