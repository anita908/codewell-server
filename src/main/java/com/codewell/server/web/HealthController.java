package com.codewell.server.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("/v1/service")
@Tag(name = "Service Controller", description = "Check service metadata")
public class HealthController
{
    @Autowired
    private Environment env;

    @GET
    @Path("/health")
    public Response getHealthStatus()
    {
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/environment")
    public String getEnvironment()
    {
        return env.getProperty("spring.profiles.active", "");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/profile")
    public List<String> getActiveProfile()
    {
        return Arrays.asList(env.getActiveProfiles());
    }
}
