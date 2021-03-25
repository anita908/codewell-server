package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.UserDto;
import com.codewell.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_NAME;
import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_SCHEME;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@Path("/v1/user")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "User Controller", description = "Manage user resources")
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
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Operation(description = "Get user data for authenticated user")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto getUserData(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String userId)
    {
        Assert.hasText(userId, "User id cannot be null");
        return userService.getUserById(userId);
    }

    @POST
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
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
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public UserDto updateUser(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String userId, final UserDto userDto)
    {
        Assert.notNull(userDto, "User payload must not be null");
        Assert.hasText(userId, "No user id provided");
        Assert.hasText(userDto.getFirstName(), "No first name provided");
        Assert.hasText(userDto.getLastName(), "No last name provided");

        return userService.updateUser(userId, userDto);
    }

    @PUT
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateCredentials")
    public Response updateLoginCredentials(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String userId, final UserDto userDto)
    {
        Assert.notNull(userDto, "User payload must not be null");
        Assert.hasText(userId, "No user id provided");
        Assert.hasText(userDto.getUsername(), "No username provided");
        Assert.hasText(userDto.getPassword(), "No password provided");

        userService.updateUsernameAndPassword(userId, userDto);
        return Response.status(Response.Status.OK).build();
    }
}
