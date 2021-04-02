package com.codewell.server.service;

import com.codewell.server.dto.GradeDto;

import java.util.List;

public interface GradesService
{
    List<GradeDto> getAllGradesForUser(final String userId);
}