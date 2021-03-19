package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.UserDto;
import com.codewell.server.service.UserService;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/user")
public class UserController
{
    private final UserService userService;

    @Inject
    public UserController(final UserService userService)
    {
        this.userService = userService;
    }

    @GET
    @JwtAuthenticationNeeded
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto getUserData(@HeaderParam("Source-User-Id") final String userId)
    {
        Assert.hasText(userId, "User id cannot be null");
        return userService.getUserById(userId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public UserDto createNewUser(final UserDto userDto)
    {
        Assert.notNull(userDto, "User payload must not be null");
        Assert.hasText(userDto.getUsername(), "No username provided");
        Assert.hasText(userDto.getPassword(), "No password provided");
        Assert.hasText(userDto.getFirstName(), "No first name provided");
        Assert.hasText(userDto.getLastName(), "No last name provided");

        return userService.createUser(userDto);
    }

    @PUT
    @JwtAuthenticationNeeded
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public UserDto updateUser(@HeaderParam("Source-User-Id") final String userId, final UserDto userDto)
    {
        Assert.notNull(userDto, "User payload must not be null");
        Assert.hasText(userId, "No user id provided");
        Assert.hasText(userDto.getFirstName(), "No first name provided");
        Assert.hasText(userDto.getLastName(), "No last name provided");

        return userService.updateUser(userId, userDto);
    }

    @PUT
    @JwtAuthenticationNeeded
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateCredentials")
    public Response updateLoginCredentials(@HeaderParam("Source-User-Id") final String userId, final UserDto userDto)
    {
        Assert.notNull(userDto, "User payload must not be null");
        Assert.hasText(userId, "No user id provided");
        Assert.hasText(userDto.getUsername(), "No username provided");
        Assert.hasText(userDto.getPassword(), "No password provided");

        userService.updateUsernameAndPassword(userId, userDto);
        return Response.status(Response.Status.OK).build();
    }
}
