package com.codewell.server.service;

import com.codewell.server.dto.UserDto;
import com.codewell.server.persistence.entity.UserCredentialsEntity;
import com.codewell.server.persistence.entity.UserEntity;
import com.codewell.server.persistence.repository.UserCredentialsRepository;
import com.codewell.server.persistence.repository.UserRepository;
import com.codewell.server.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String NOT_ADMIN = "false";

    @Inject
    public UserServiceImpl(final UserRepository userRepository,
                           final UserCredentialsRepository userCredentialsRepository,
                           final PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
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
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Assert.isTrue(UserUtil.hasValidUsername(newUser), "Username is not valid");
        Assert.isTrue(UserUtil.hasValidEmail(newUser), "Email is not valid");
        Assert.isTrue(UserUtil.hasValidAge(newUser), "Age must be greater than 0");
        Assert.isTrue(UserUtil.hasValidAddress(newUser), "Address must only contain letters");

        if (StringUtils.isEmpty(newUser.getIsAdmin()))
        {
            newUser.setIsAdmin(NOT_ADMIN);
        }

        final UserEntity newUserEntity = this.mapToUserEntity(newUser);
        LOGGER.info("Inserting new user into users table: {}", newUserEntity);
        userRepository.insert(newUserEntity);

        final UserCredentialsEntity newCredentialsEntity = this.mapToUserCredentialsEntity(newUser);
        LOGGER.info("Inserting new user credentials into credentials table: {}", newCredentialsEntity);
        userCredentialsRepository.insert(newCredentialsEntity);

        return this.getUserById(userId);
    }

    @Override
    public UserDto updateUser(final String userId, final UserDto userDto)
    {
        Assert.isTrue(UserUtil.hasValidEmail(userDto), "Email is not valid");
        Assert.isTrue(UserUtil.hasValidAge(userDto), "Age must be greater than 0");
        Assert.isTrue(UserUtil.hasValidAddress(userDto), "Address must only contain letters");

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

    @Override
    public void updateUsernameAndPassword(final String userId, final UserDto userDto)
    {
        Assert.isTrue(UserUtil.hasValidUsername(userDto), "Username is not valid");

        final UserCredentialsEntity originalCredentials = userCredentialsRepository.select(userId);
        if (originalCredentials == null)
        {
            throw new IllegalArgumentException(String.format("No login credentials found for user id: %s", userId));
        }

        originalCredentials.setUsername(userDto.getUsername());
        originalCredentials.setPassword(passwordEncoder.encode(userDto.getPassword()));
        originalCredentials.setUpdatedAt(OffsetDateTime.now());

        userCredentialsRepository.update(originalCredentials);
    }

    private UserEntity mapToUserEntity(final UserDto userDto)
    {
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

    private UserCredentialsEntity mapToUserCredentialsEntity(final UserDto userDto)
    {
        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();
        userCredentialsEntity.setUserId(userDto.getUserId());
        userCredentialsEntity.setUsername(userDto.getUsername());
        userCredentialsEntity.setPassword(userDto.getPassword());
        final OffsetDateTime currentTime = OffsetDateTime.now();
        userCredentialsEntity.setCreatedAt(currentTime);
        userCredentialsEntity.setUpdatedAt(currentTime);
        return userCredentialsEntity;
    }

    private UserDto mapToUserDto(final UserEntity userEntity)
    {
        return Optional.ofNullable(userEntity)
            .map(entity -> UserDto.newBuilder()
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
