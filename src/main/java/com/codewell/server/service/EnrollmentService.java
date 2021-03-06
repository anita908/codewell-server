package com.codewell.server.service;

import com.codewell.server.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService
{
    List<EnrollmentDto> getEnrollmentsByUser(final String userId);
    List<EnrollmentDto> getEnrollmentsBySession(final Integer sessionId);
    EnrollmentDto getEnrollmentByUserAndSession(final String userId, final Integer sessionId);
    EnrollmentDto enrollStudentToSession(final String userId, final Integer sessionId);
    EnrollmentDto updateEnrollmentRecord(final EnrollmentDto enrollmentDto);
}
