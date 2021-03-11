package com.codewell.server.web;

import com.codewell.server.dto.HomeworkVideoDto;
import com.codewell.server.service.HomeworkService;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v1/homework")
public class HomeworkController
{
    private final HomeworkService homeworkService;

    @Inject
    public HomeworkController(final HomeworkService homeworkService)
    {
        this.homeworkService = homeworkService;
    }

    @GET
    @Path("/videos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HomeworkVideoDto> getVideos(@QueryParam("homeworkId") final int homeworkId)
    {
        Assert.isTrue(homeworkId > 0, "Homework id must be provided");
        return homeworkService.getVideosForHomework(homeworkId);
    }
}
