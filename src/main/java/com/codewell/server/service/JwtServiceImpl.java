package com.codewell.server.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.codewell.server.persistence.entity.UserAuthEntity;
import com.codewell.server.persistence.repository.UserAuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@Singleton
public class JwtServiceImpl implements JwtService
{
    private final UserAuthRepository userAuthRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtServiceImpl.class);
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("secret");
    private static final String ISSUER = "codewell-server";
    private static final String SALT = "salt";
    private static final String EXPIRATION = "expirationDate";

    public JwtServiceImpl(final UserAuthRepository userAuthRepository)
    {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public String assignJwtToken(final String userId)
    {
        try
        {
            final UserAuthEntity newAuthEntity = new UserAuthEntity();
            newAuthEntity.setUserId(userId);

            final OffsetDateTime createdAt = OffsetDateTime.now();
            final OffsetDateTime expireAt = createdAt.plusDays(7L);
            newAuthEntity.setCreatedAt(createdAt);
            newAuthEntity.setExpireDate(expireAt);

            final String salt = UUID.randomUUID().toString();
            final String token = JWT.create()
                .withIssuer(ISSUER)
                .withClaim(EXPIRATION, expireAt.toString())
                .withClaim(SALT, salt)
                .sign(ALGORITHM);
            newAuthEntity.setToken(token);
            newAuthEntity.setSalt(salt);

            userAuthRepository.update(newAuthEntity);
            return token;
        }
        catch (Exception exception)
        {
            LOGGER.error("Could not update user_auth record for user id: {}", userId);
            throw exception;
        }
    }
}
