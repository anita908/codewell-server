package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.EnrollmentDto;
import com.codewell.server.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_NAME;
import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_SCHEME;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@Path("/v1/enrollment")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "Enrollment Controller", description = "Manage enrollment resources")
public class EnrollmentController
{
    private final EnrollmentService enrollmentService;

    @Inject
    public EnrollmentController(final EnrollmentService enrollmentService)
    {
        this.enrollmentService = enrollmentService;
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    public List<EnrollmentDto> getEnrollmentsForUser(@Parameter(hidden =true) @HeaderParam("Source-User-Id") final String userId)
    {
        Assert.hasText(userId, "User id must be provided");
        return enrollmentService.getEnrollmentsByUser(userId);
    }
}
