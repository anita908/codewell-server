package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.EnrollmentDto;
import com.codewell.server.service.EnrollmentService;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v1/enrollment")
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<EnrollmentDto> getEnrollmentsForUser(@HeaderParam("Source-User-Id") final String userId)
    {
        Assert.hasText(userId, "User id must be provided");
        return enrollmentService.getEnrollmentsByUser(userId);
    }

    @POST
    @JwtAuthenticationNeeded
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/enroll")
    public EnrollmentDto enrollStudent(@HeaderParam("Source-User-Id") final String userId, @QueryParam("sessionId") final int sessionId)
    {
        Assert.hasText(userId, "User id must be provided");
        Assert.isTrue(sessionId > 0, "Session id must be provided");

        return enrollmentService.enrollStudentToSession(userId, sessionId);
    }
}
