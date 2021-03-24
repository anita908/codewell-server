package com.codewell.server.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.codewell.server.annotation.JwtAuthenticationNeeded;
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
        final String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null)
        {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("No auth token provided").build());
            return;
        }
        try
        {
            final String jwtToken = authHeader.substring(BEARER.length()).trim();
            final Claim userIdClaim = JWT.decode(jwtToken).getClaim(USER_ID);
            if (userIdClaim.isNull())
            {
                final String message = "No user id provided in jwt";
                LOGGER.info(message);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
                return;
            }
            final String userId = userIdClaim.asString();
            final UserTokenEntity userTokenEntity = userTokenRepository.select(userId);
            if (userTokenEntity == null)
            {
                final String message = String.format("No prior token found for user id: %s", userId);
                LOGGER.info(message);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
                return;
            }
            if (!jwtToken.equals(userTokenEntity.getToken()))
            {
                final String message = "Received jwt that doesn't match with database record";
                LOGGER.info(message);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
                return;
            }
            JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .withClaim(USER_ID, userTokenEntity.getUserId())
                .withClaim(SALT, userTokenEntity.getSalt())
                .build()
                .verify(jwtToken);
            if (userTokenEntity.getExpireDate().isBefore(OffsetDateTime.now()))
            {
                final String message = String.format("Received expired JWT token: %s", jwtToken);
                LOGGER.info(message);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
            }
            requestContext.getHeaders().add("Source-User-Id", userId);
        }
        catch (JWTDecodeException exception)
        {
            final String message = String.format("JWT is in wrong format: %s", exception.getMessage());
            LOGGER.error(message);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
        }
        catch (JWTVerificationException exception)
        {
            final String message = String.format("JWT verification failed: %s", exception.getMessage());
            LOGGER.error(message);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
        }
    }
}
