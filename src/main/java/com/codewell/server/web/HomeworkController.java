package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.HomeworkDto;
import com.codewell.server.dto.HomeworkVideoDto;
import com.codewell.server.service.HomeworkService;
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

@Path("/v1/homework")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "Homework Controller", description = "Manage homework resources")
public class HomeworkController
{
    private final HomeworkService homeworkService;

    @Inject
    public HomeworkController(final HomeworkService homeworkService)
    {
        this.homeworkService = homeworkService;
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    public List<HomeworkDto> getHomeworks(@QueryParam("courseId") final int courseId)
    {
        Assert.isTrue(courseId > 0, "Course id must be provided");
        return homeworkService.getHomeworksForCourse(courseId);
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/course/{courseId}/chapter/{chapterNo}")
    public List<HomeworkDto> getHomeworksForCourseChapter(@PathParam("courseId") final int courseId, @PathParam("chapterNo") final int chapterNo)
    {
        Assert.isTrue(courseId > 0, "Course id must be provided");
        Assert.isTrue(chapterNo > 0, "Chapter no must be provided");
        return homeworkService.getHomeworksForCourseAndChapter(courseId, chapterNo);
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/videos")
    public List<HomeworkVideoDto> getHomeworkVideos(@QueryParam("homeworkId") final int homeworkId)
    {
        Assert.isTrue(homeworkId > 0, "Homework id must be provided");
        return homeworkService.getVideosForHomework(homeworkId);
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/videos/course/{courseId}/chapter/{chapterNo}")
    public List<HomeworkVideoDto> getHomeworkVideosForCourseChapter(@PathParam("courseId") final int courseId, @PathParam("chapterNo") final int chapterNo)
    {
        Assert.isTrue(courseId > 0, "Course id must be provided");
        Assert.isTrue(chapterNo > 0, "Chapter no must be provided");
        return homeworkService.getVideosForCourseAndChapter(courseId, chapterNo);
    }

    @GET
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/videos/course")
    public List<HomeworkVideoDto> getHomeworkVideosForCourse(@QueryParam("courseId") final int courseId)
    {
        Assert.isTrue(courseId > 0, "Course id must be provided");
        return homeworkService.getVideosForCourse(courseId);
    }
}
