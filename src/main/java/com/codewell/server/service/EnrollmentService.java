package com.codewell.server.service;

import com.codewell.server.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService
{
    List<EnrollmentDto> getEnrollmentsByUser(final String userId);
    EnrollmentDto enrollStudentToSession(final String userId, final int sessionId);
}
