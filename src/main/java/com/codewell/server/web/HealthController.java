package com.codewell.server.web;

import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/v1/health")
@Tag(name = "Health Controller", description = "Check stack health")
public class HealthController
{
    @GET
    public Response getHealthStatus()
    {
        return Response.status(Response.Status.OK).build();
    }
}
