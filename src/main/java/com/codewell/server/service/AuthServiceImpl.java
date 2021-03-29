package com.codewell.server.service;

import com.codewell.server.dto.AuthTokenDto;
import com.codewell.server.persistence.entity.UserCredentialsEntity;
import com.codewell.server.persistence.repository.UserCredentialsRepository;
import com.codewell.server.persistence.repository.UserTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.OffsetDateTime;

@Named
@Singleton
public class AuthServiceImpl implements AuthService
{
    private final JwtService jwtService;
    private final UserCredentialsRepository userCredentialsRepository;
    private final UserTokenRepository userTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Inject
    public AuthServiceImpl(final JwtService jwtService,
                           final UserCredentialsRepository userCredentialsRepository,
                           final UserTokenRepository userTokenRepository,
                           final PasswordEncoder passwordEncoder)
    {
        this.jwtService = jwtService;
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
        final AuthTokenDto authTokenDto = new AuthTokenDto();
        authTokenDto.setResult(SUCCESS);
        authTokenDto.setJwt(jwtService.assignJwtToken(savedCredentials.getUserId()));
        authTokenDto.setTokenAssignDate(OffsetDateTime.now());
        return authTokenDto;
    }

    @Override
    public AuthTokenDto refreshUser(final String userId)
    {
        return new AuthTokenDto(SUCCESS, jwtService.assignJwtToken(userId), OffsetDateTime.now());
    }

    @Override
    public void logoutUser(final String userId)
    {
        userTokenRepository.delete(userId);
    }
}
