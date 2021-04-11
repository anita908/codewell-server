package com.codewell.server.service;

import com.codewell.server.dto.GradeDto;
import com.codewell.server.persistence.entity.SessionEntity;

import java.util.List;

public interface GradesService
{
    void createDefaultGradesForUser(final String userId, final SessionEntity session);
    List<GradeDto> getGradesForUser(final String userId, final Integer sessionId);
    GradeDto modifyGrade(final String userId, final Integer sessionId, final GradeDto gradeDto);
    List<GradeDto> bulkModifyGrades(final String userId, final Integer sessionId, final List<GradeDto> gradeDtos) throws InterruptedException;
}