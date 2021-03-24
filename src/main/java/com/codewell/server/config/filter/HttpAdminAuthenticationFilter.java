package com.codewell.server.config.filter;

import com.codewell.server.annotation.AdminAuthenticationNeeded;
import com.codewell.server.persistence.entity.UserEntity;
import com.codewell.server.persistence.repository.UserRepository;
import com.codewell.server.persistence.repository.UserTokenRepository;
import org.glassfish.jersey.server.ContainerRequest;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@AdminAuthenticationNeeded
@Priority(Priorities.AUTHORIZATION)
public class HttpAdminAuthenticationFilter extends HttpAuthenticationFilter
{
    private final UserRepository userRepository;

    private static final String USER_ID_HEADER = "Source-User-Id";
    private static final String TRUE = "true";

    @Inject
    public HttpAdminAuthenticationFilter(final UserRepository userRepository, final UserTokenRepository userTokenRepository)
    {
        super(userTokenRepository);
        this.userRepository = userRepository;
    }

    @Override
    public void filter(ContainerRequestContext requestContext)
    {
        super.filter(requestContext);
        if (((ContainerRequest) requestContext).getAbortResponse() != null)
        {
            return;
        }
        final String userId = requestContext.getHeaderString(USER_ID_HEADER);
        final UserEntity user = userRepository.selectByUserId(userId);
        if (!TRUE.equals(user.getIsAdmin()))
        {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User is not an admin").build());
        }
    }
}
