package com.codewell.server.web;

import com.codewell.server.persistence.entity.UserEntity;
import com.codewell.server.persistence.repository.UserRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;
import java.util.UUID;

@Path("v1/user")
public class UserController
{
    private final UserRepository userRepository;

    @Inject
    public UserController(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test()
    {
        return "Welcome to our server";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public UserEntity createNewUser(final UserEntity userEntity)
    {
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setCreatedAt(OffsetDateTime.now());
        userEntity.setUpdatedAt(OffsetDateTime.now());
        userRepository.insert(userEntity);
        return userEntity;
    }
}
