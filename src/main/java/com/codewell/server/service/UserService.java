package com.codewell.server.service;

import com.codewell.server.dto.UserDto;

import java.util.List;

public interface UserService
{
    List<UserDto> getAllUsers();
    UserDto getUserById(final String userId);
    UserDto createUser(final UserDto userDto);
    UserDto updateUser(final String userId, final UserDto userDto);
    void updateUsernameAndPassword(final String userId, final UserDto userDto);
}
