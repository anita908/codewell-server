package com.codewell.server.web;

import com.codewell.server.annotation.AdminAuthenticationNeeded;
import com.codewell.server.dto.UserDto;
import com.codewell.server.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_NAME;
import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_SCHEME;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@Path("/v1/admin")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "Admin Controller", description = "Manage admin resources")
public class AdminController
{
    private final UserService userService;

    @Inject
    public AdminController(final UserService userService)
    {
        this.userService = userService;
    }

    @POST
    @AdminAuthenticationNeeded
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public UserDto createNewUser(final UserDto userDto)
    {
        Assert.notNull(userDto, "User payload must not be null");
        Assert.hasText(userDto.getUsername(), "No username provided");
        Assert.hasText(userDto.getPassword(), "No password provided");
        Assert.hasText(userDto.getEmail(), "No email provided");
        Assert.hasText(userDto.getFirstName(), "No first name provided");

        return userService.createUser(userDto);
    }

    @GET
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public List<UserDto> getAllUsers()
    {
        return userService.getAllUsers();
    }
}
