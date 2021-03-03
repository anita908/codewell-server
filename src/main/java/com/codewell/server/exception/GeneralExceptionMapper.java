package com.codewell.server.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception>
{
    @Override
    public Response toResponse(Exception exception)
    {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(new ExceptionResponse(exception))
            .build();
    }
}