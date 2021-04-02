package com.codewell.server.web;

import com.codewell.server.annotation.JwtAuthenticationNeeded;
import com.codewell.server.dto.AuthTokenDto;
import com.codewell.server.dto.LoginDto;
import com.codewell.server.dto.UserCredentialsDto;
import com.codewell.server.service.AuthService;
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

@Path("/v1/auth")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "Auth Controller", description = "Manage auth resources")
public class AuthController
{
    private final AuthService authService;

    private static final String RESET_LINK_LOCAL_URL = "http://localhost:3000/resetPassword";
    private static final String RESET_LINK_PRODUCTION_UL = "https://codewell-portal.web.app/resetPassword";

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

    @DELETE
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Path("/logout")
    public Response logout(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String userId)
    {
        Assert.hasText(userId, "Username must be provided");
        authService.logoutUser(userId);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/refresh")
    public AuthTokenDto refresh(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String userId)
    {
        Assert.hasText(userId, "No userId provided");
        return authService.refreshUser(userId);
    }

    @PUT
    @JwtAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateCredentials")
    public Response updatePassword(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String userId, final UserCredentialsDto userCredentialsDto)
    {
        Assert.notNull(userCredentialsDto, "User payload must not be null");
        Assert.hasText(userId, "No user id provided");
        Assert.hasText(userCredentialsDto.getUsername(), "No username provided");
        Assert.hasText(userCredentialsDto.getPassword(), "No password provided");

        authService.updatePassword(userId, userCredentialsDto);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reset/sendEmail/{emailAddress}")
    public Response sendResetEmail(@PathParam("emailAddress") final String emailAddress, @QueryParam("local") final boolean local) throws Exception
    {
        Assert.hasText(emailAddress, "No email provided");

        final String linkBaseUrl = local ? RESET_LINK_LOCAL_URL : RESET_LINK_PRODUCTION_UL;
        authService.sendPasswordResetEmail(emailAddress, linkBaseUrl);
        return Response.status(Response.Status.OK).entity(String.format("Successfully sent password reset email to: %s", emailAddress)).build();
    }
}
