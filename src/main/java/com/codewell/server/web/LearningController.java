package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.UserLearningModel;
import com.codewell.server.service.UserLearningService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_NAME;
import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_SCHEME;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@Path("/v1/learning")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "User Learning Controller", description = "Manage user learning resources")
public class LearningController
{
    private final UserLearningService userLearningService;

    @Inject
    public LearningController(final UserLearningService userLearningService)
    {
        this.userLearningService = userLearningService;
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/progress")
    public UserLearningModel getUserLearningProgress(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String userId) throws Exception
    {
        Assert.hasText(userId, "User id cannot be null");
        return userLearningService.getUserLearningModel(userId);
    }
}
