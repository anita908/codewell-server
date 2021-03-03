package com.codewell.server.service;

import com.auth0.jwt.JWT;
import com.codewell.server.dto.AuthTokenDto;
import com.codewell.server.dto.UserDto;
import com.codewell.server.persistence.repository.UserAuthRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.OffsetDateTime;

@Component
@Singleton
public class AuthServiceImpl implements AuthService
{
    private final UserService userService;
    private final JwtService jwtService;
    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String SUCCESS = "SUCCESS";

    @Inject
    public AuthServiceImpl(final UserService userService, final JwtService jwtService, final UserAuthRepository userAuthRepository, final PasswordEncoder passwordEncoder)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthTokenDto loginUser(final String username, final String password)
    {
        final UserDto userDto = userService.getUserByUsername(username);
        if (userDto == null)
        {
            throw new IllegalArgumentException(String.format("User data for username: %s doesn't exist", username));

        }
        if (!passwordEncoder.matches(password, userDto.getPassword()))
        {
            throw new IllegalArgumentException(String.format("Incorrect password for username: %s", username));
        }
        final AuthTokenDto authTokenDto = new AuthTokenDto();
        authTokenDto.setResult(SUCCESS);
        authTokenDto.setJwt(jwtService.assignJwtToken(userDto.getUserId()));
        authTokenDto.setTokenAssignDate(OffsetDateTime.now());
        return authTokenDto;
    }

    public AuthTokenDto refreshUser(final String userId)
    {
        return new AuthTokenDto(SUCCESS, jwtService.assignJwtToken(userId), OffsetDateTime.now());
    }

    @Override
    public void logoutUser(final String username)
    {
        final UserDto userDto = userService.getUserByUsername(username);
        if (userDto != null)
        {
            userAuthRepository.delete(userDto.getUserId());
        }
    }
}
