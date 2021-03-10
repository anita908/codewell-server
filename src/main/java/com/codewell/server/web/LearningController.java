package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.UserLearningModel;
import com.codewell.server.service.UserLearningService;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/learning")
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/progress")
    public UserLearningModel getUserHomepageData(@HeaderParam("Source-User-Id") final String userId) throws Exception
    {
        Assert.hasText(userId, "User id cannot be null");
        return userLearningService.getUserLearningModel(userId);
    }
}
