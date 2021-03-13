package com.codewell.server.web;

import com.codewell.server.annotation.AdminAuthenticationNeeded;
import com.codewell.server.dto.UserDto;
import com.codewell.server.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v1/admin")
public class AdminController
{
    private final UserService userService;

    @Inject
    public AdminController(final UserService userService)
    {
        this.userService = userService;
    }

    @GET
    @AdminAuthenticationNeeded
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public List<UserDto> getAllUsers()
    {
        return userService.getAllUsers();
    }
}
