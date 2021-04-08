package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.GradeDto;
import com.codewell.server.service.GradesService;
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

@Path("/v1/grades")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "Grades Controller", description = "Manage grades resources")
public class GradesController
{
    private final GradesService gradeService;

    @Inject
    public GradesController(final GradesService gradeService)
    {
        this.gradeService = gradeService;
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    public List<GradeDto> getGrades(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String userId,
                                    @QueryParam("sessionId") final Integer sessionId)
    {
        Assert.hasText(userId, "User id not provided");
        return gradeService.getGradesForUser(userId, sessionId);
    }
}
