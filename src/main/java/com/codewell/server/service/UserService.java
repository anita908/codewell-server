package com.codewell.server.service;

import com.codewell.server.dto.UserDto;

public interface UserService
{
    UserDto getUserById(final String userId);
    UserDto createUser(final UserDto userDto);
    UserDto updateUser(final UserDto userDto);
    void updateUsernameAndPassword(final String userId, final UserDto userDto);
}
