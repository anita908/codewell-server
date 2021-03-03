package com.codewell.server.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.persistence.entity.UserAuthEntity;
import com.codewell.server.persistence.repository.UserAuthRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.time.OffsetDateTime;

@Provider
@JwtAuthenticationNeeded
@Priority(Priorities.AUTHENTICATION)
public class HttpAuthenticationConfig implements ContainerRequestFilter
{
    private final UserAuthRepository userAuthRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAuthenticationConfig.class);
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("secret");
    private static final String BEARER = "Bearer";
    private static final String ISSUER = "codewell-server";
    private static final String USER_ID = "userId";
    private static final String SALT = "salt";

    @Inject
    public HttpAuthenticationConfig(final UserAuthRepository userAuthRepository)
    {
        this.userAuthRepository = userAuthRepository;
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
        final String jwtToken = authHeader.substring(BEARER.length()).trim();
        String userId;
        try
        {
            MultivaluedMap<String, String> pathParams = requestContext.getUriInfo().getPathParameters();
            MultivaluedMap<String, String> queryParams = requestContext.getUriInfo().getQueryParameters();
            userId = pathParams.getFirst(USER_ID);
            if (userId == null)
            {
                userId = queryParams.getFirst(USER_ID);
            }
            if (userId == null)
            {
                ContainerRequest cr = (ContainerRequest) requestContext;
                cr.bufferEntity();
                JsonNode jsonNode = cr.readEntity(JsonNode.class);
                if (jsonNode == null)
                {
                    userId = null;
                }
                else
                {
                    userId = jsonNode.has(USER_ID) ? jsonNode.get(USER_ID).asText() : null;
                }

            }
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            throw new IllegalArgumentException(exception.getMessage());
        }
        if (userId == null)
        {
            throw new IllegalArgumentException("No user id provided for jwt authenticated request");
        }
        try
        {
            final UserAuthEntity userAuthEntity = userAuthRepository.select(userId);
            if (userAuthEntity == null)
            {
                final String message = String.format("No prior token found for user id: %s", userId);
                LOGGER.info(message);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
                return;
            }
            if (!jwtToken.equals(userAuthEntity.getToken()))
            {
                final String message = "Received jwt that doesn't match with database record";
                LOGGER.info(message);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
                return;
            }
            JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .withClaim(SALT, userAuthEntity.getSalt())
                .build()
                .verify(jwtToken);
            if (userAuthEntity.getExpireDate().isBefore(OffsetDateTime.now()))
            {
                final String message = String.format("Received expired JWT token: %s", jwtToken);
                LOGGER.info(message);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
            }
        }
        catch (JWTVerificationException exception)
        {
            LOGGER.error(exception.getMessage());
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).build());
        }
    }
}
