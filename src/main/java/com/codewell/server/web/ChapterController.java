package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.ChapterDto;
import com.codewell.server.service.CourseChapterService;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v1/chapter")
public class ChapterController
{
    private final CourseChapterService courseChapterService;

    @Inject
    public ChapterController(final CourseChapterService courseChapterService)
    {
        this.courseChapterService = courseChapterService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JwtAuthenticationNeeded
    public List<ChapterDto> getChapters(@QueryParam("courseId") final int courseId)
    {
        Assert.isTrue(courseId > 0, "Course id must be provided");
        return courseChapterService.getChaptersForCourse(courseId);
    }
}
