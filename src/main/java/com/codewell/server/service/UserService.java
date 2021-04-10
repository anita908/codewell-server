package com.codewell.server.service;

import com.codewell.server.dto.UserDto;

import java.util.List;

public interface UserService
{
    List<UserDto> getAllUsers();
    List<UserDto> getUsersInSession(final Integer sessionId);
    UserDto getUserById(final String userId);
    UserDto createUser(final UserDto userDto);
    UserDto updateUser(final String userId, final UserDto userDto);
}
