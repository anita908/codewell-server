package com.codewell.server.service;

import com.codewell.server.dto.AuthTokenDto;
import com.codewell.server.dto.UserCredentialsDto;
import com.codewell.server.persistence.entity.UserCredentialsEntity;
import com.codewell.server.persistence.entity.UserEntity;
import com.codewell.server.persistence.repository.UserCredentialsRepository;
import com.codewell.server.persistence.repository.UserRepository;
import com.codewell.server.persistence.repository.UserTokenRepository;
import com.codewell.server.util.DataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.OffsetDateTime;

@Named
@Singleton
public class AuthServiceImpl implements AuthService
{
    private final JwtService jwtService;
    private final MailingService mailingService;
    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final UserTokenRepository userTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String RESET_PASSWORD_SUBJECT = "Password Reset";
    private static final String RESET_PASSWORD_MESSAGE = "<h3>Hi %s:</h3>" +
        "<br></br>" +
        "<p>We have received your request to reset your password. Please click on the link below to initiate the reset process.</p>" +
        "<br></br>" +
        "<a href='%s'>Reset Password</a>";

    @Inject
    public AuthServiceImpl(final JwtService jwtService,
                           final MailingService mailingService,
                           final UserRepository userRepository,
                           final UserCredentialsRepository userCredentialsRepository,
                           final UserTokenRepository userTokenRepository,
                           final PasswordEncoder passwordEncoder)
    {
        this.jwtService = jwtService;
        this.mailingService = mailingService;
        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.userTokenRepository = userTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthTokenDto loginUser(final String username, final String password)
    {
        final UserCredentialsEntity savedCredentials = userCredentialsRepository.selectByUsername(username);
        if (savedCredentials == null)
        {
            throw new IllegalArgumentException(String.format("Username: %s doesn't exist", username));

        }
        if (!passwordEncoder.matches(password, savedCredentials.getPassword()))
        {
            throw new IllegalArgumentException(String.format("Incorrect password for username: %s", username));
        }
        return this.refreshUser(savedCredentials.getUserId());
    }

    @Override
    public void logoutUser(final String userId)
    {
        userTokenRepository.delete(userId);
    }

    @Override
    public AuthTokenDto refreshUser(final String userId)
    {
        final UserEntity userData = userRepository.selectByUserId(userId);
        if (userData == null)
        {
            throw new IllegalArgumentException(String.format("User id: %s doesn't exist", userId));
        }
        return new AuthTokenDto(SUCCESS, jwtService.assignJwtToken(userId), Boolean.parseBoolean(userData.getIsAdmin()), OffsetDateTime.now());
    }

    @Override
    public void createUsernameAndPassword(final String userId, final UserCredentialsDto userCredentialsDto)
    {
        Assert.hasText(userId, "User id cannot be empty or null");

        userCredentialsDto.setPassword(passwordEncoder.encode(userCredentialsDto.getPassword()));
        final UserCredentialsEntity newCredentialsEntity = this.mapToUserCredentialsEntity(userCredentialsDto);
        newCredentialsEntity.setUserId(userId);
        LOGGER.info("Inserting new user credentials into credentials table: {}", newCredentialsEntity.toString());

        userCredentialsRepository.insert(newCredentialsEntity);
    }

    @Override
    public void updatePassword(final String userId, final UserCredentialsDto userCredentialsDto)
    {
        Assert.isTrue(DataValidator.isValidUsername(userCredentialsDto.getUsername()), "Username is not valid");

        final UserCredentialsEntity originalCredentials = userCredentialsRepository.select(userId);
        if (originalCredentials == null)
        {
            throw new IllegalArgumentException(String.format("No login credentials found for user id: %s", userId));
        }
        if (!originalCredentials.getUsername().equals(userCredentialsDto.getUsername()))
        {
            throw new IllegalArgumentException(String.format("Supplied username does not belong to userId: %s", userId));
        }

        originalCredentials.setUsername(userCredentialsDto.getUsername());
        originalCredentials.setPassword(passwordEncoder.encode(userCredentialsDto.getPassword()));
        originalCredentials.setUpdatedAt(OffsetDateTime.now());

        userCredentialsRepository.update(originalCredentials);
    }

    @Override
    public void sendPasswordResetEmail(final String emailAddress, final String linkBaseUrl) throws Exception
    {
        Assert.isTrue(DataValidator.isValidEmail(emailAddress), "Email is not valid");

        final UserEntity userEntity = userRepository.selectByEmail(emailAddress);
        if (userEntity == null)
        {
            throw new IllegalArgumentException(String.format("User profile could not be found for given email: %s", emailAddress) );
        }

        final String jwtToken = jwtService.assignJwtToken(userEntity.getUserId());
        final String fullName = String.format("%s %s", userEntity.getFirstName(), userEntity.getLastName());
        final String linkUrl = String.format("%s?token=%s", linkBaseUrl, jwtToken);
        final String message = String.format(RESET_PASSWORD_MESSAGE, fullName, linkUrl);
        mailingService.sendEmail(emailAddress, RESET_PASSWORD_SUBJECT, message);
    }

    private UserCredentialsEntity mapToUserCredentialsEntity(final UserCredentialsDto userCredentialsDto)
    {
        if (userCredentialsDto == null)
        {
            return null;
        }

        final UserCredentialsEntity userCredentialsEntity = new UserCredentialsEntity();
        userCredentialsEntity.setUsername(userCredentialsDto.getUsername());
        userCredentialsEntity.setPassword(userCredentialsDto.getPassword());
        final OffsetDateTime currentTime = OffsetDateTime.now();
        userCredentialsEntity.setCreatedAt(currentTime);
        userCredentialsEntity.setUpdatedAt(currentTime);
        return userCredentialsEntity;
    }
}
