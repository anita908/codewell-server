package com.codewell.server.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.exception.ExceptionResponse;
import com.codewell.server.persistence.entity.UserTokenEntity;
import com.codewell.server.persistence.repository.UserTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.time.OffsetDateTime;

@Provider
@JwtAuthenticationNeeded
@Priority(Priorities.AUTHENTICATION)
public class HttpAuthenticationFilter implements ContainerRequestFilter
{
    private final UserTokenRepository userTokenRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAuthenticationFilter.class);
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("secret");
    private static final String BEARER = "Bearer";
    private static final String ISSUER = "codewell-server";
    private static final String USER_ID = "userId";
    private static final String SALT = "salt";

    @Inject
    public HttpAuthenticationFilter(final UserTokenRepository userTokenRepository)
    {
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IllegalArgumentException
    {
        try
        {
            final String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (authHeader == null)
            {
                throw new IllegalArgumentException("No auth token provided");
            }
            final String jwtToken = authHeader.substring(BEARER.length()).trim();
            final Claim userIdClaim = JWT.decode(jwtToken).getClaim(USER_ID);
            if (userIdClaim.isNull())
            {
                throw new IllegalArgumentException("No user id provided in jwt");
            }
            final String userId = userIdClaim.asString();
            final UserTokenEntity userTokenEntity = userTokenRepository.select(userId);
            if (userTokenEntity == null)
            {
                throw new IllegalArgumentException(String.format("No prior token found for user id: %s", userId));
            }
            if (!jwtToken.equals(userTokenEntity.getToken()))
            {
                throw new IllegalArgumentException("Received jwt that doesn't match with database record");
            }
            JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .withClaim(USER_ID, userTokenEntity.getUserId())
                .withClaim(SALT, userTokenEntity.getSalt())
                .build()
                .verify(jwtToken);
            if (userTokenEntity.getExpireDate().isBefore(OffsetDateTime.now()))
            {
                throw new IllegalArgumentException(String.format("Received expired JWT token: %s", jwtToken));
            }
            requestContext.getHeaders().add("Source-User-Id", userId);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ExceptionResponse(exception))
                .build());
        }
    }
}
