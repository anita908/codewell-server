package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.AuthTokenDto;
import com.codewell.server.dto.LoginDto;
import com.codewell.server.service.AuthService;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/auth")
public class AuthController
{
    private final AuthService authService;

    @Inject
    public AuthController(final AuthService authService)
    {
        this.authService = authService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public AuthTokenDto login(final LoginDto loginDto)
    {
        Assert.notNull(loginDto, "LoginDto must not be null");
        Assert.hasText(loginDto.getUsername(), "Username not provided");
        Assert.hasText(loginDto.getPassword(), "Password not provided");
        return authService.loginUser(loginDto.getUsername(), loginDto.getPassword());
    }

    @POST
    @JwtAuthenticationNeeded
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/refresh")
    public AuthTokenDto refresh(@HeaderParam("Source-User-Id") final String userId)
    {
        Assert.hasText(userId, "No userId provided");
        return authService.refreshUser(userId);
    }

    @DELETE
    @Path("/logout/{username}")
    public Response logout(@PathParam("username") final String username)
    {
        Assert.hasText(username, "Username must be provided");
        authService.logoutUser(username);
        return Response.status(Response.Status.OK).build();
    }
}
