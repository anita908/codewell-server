package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.ChapterDto;
import com.codewell.server.service.CourseChapterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_NAME;
import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_SCHEME;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@Path("/v1/chapter")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "Chapter Controller", description = "Manage chapter resources")
public class ChapterController
{
    private final CourseChapterService courseChapterService;

    @Inject
    public ChapterController(final CourseChapterService courseChapterService)
    {
        this.courseChapterService = courseChapterService;
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    public List<ChapterDto> getChapters(@QueryParam("courseId") final int courseId)
    {
        Assert.isTrue(courseId > 0, "Course id must be provided");
        return courseChapterService.getChaptersForCourse(courseId);
    }
}
