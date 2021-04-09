package com.codewell.server.service;

import com.codewell.server.dto.SessionDto;

import java.util.List;

public interface SessionService
{
    List<SessionDto> getSessionsTaughtByAdmin(final String adminUserId);
}
