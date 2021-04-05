package com.codewell.server.service;

import com.codewell.server.dto.GradeDto;
import com.codewell.server.persistence.entity.SessionEntity;

import java.util.List;

public interface GradesService
{
    void createDefaultGradesForUser(final String userId, final SessionEntity session);
    List<GradeDto> getAllGradesForUser(final String userId);
}