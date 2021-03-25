package com.codewell.server.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/v1/default")
public class DefaultController
{
    @GET
    public Response getHealthStatus()
    {
        return Response.status(Response.Status.OK).build();
    }
}
