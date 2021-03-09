package com.codewell.server.service;

import com.codewell.server.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService
{
    List<EnrollmentDto> getEnrollmentsForUser(final String userId);
}
