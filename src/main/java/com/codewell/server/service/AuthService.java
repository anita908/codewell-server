package com.codewell.server.service;

import com.codewell.server.dto.AuthTokenDto;

public interface AuthService
{
    AuthTokenDto loginUser(final String username, final String password);
    AuthTokenDto refreshUser(final String userId);
    void logoutUser(final String userId);
}
