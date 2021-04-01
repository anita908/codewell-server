package com.codewell.server.service;

import com.codewell.server.dto.AuthTokenDto;
import com.codewell.server.dto.UserCredentialsDto;

public interface AuthService
{
    AuthTokenDto loginUser(final String username, final String password);
    void logoutUser(final String userId);
    AuthTokenDto refreshUser(final String userId);
    void createUsernameAndPassword(final String userId, final UserCredentialsDto userCredentialsDto);
    void updatePassword(final String userId, final UserCredentialsDto userCredentialsDto);
    void sendPasswordResetEmail(final String userId) throws Exception;
}
