package com.codewell.server.service;

import com.codewell.server.persistence.repository.EnrollmentRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Singleton;

@Component
@Singleton
public class EnrollmentServiceImpl implements EnrollmentService
{
    private final EnrollmentRepository enrollmentRepository;

    @Inject
    public EnrollmentServiceImpl(final EnrollmentRepository enrollmentRepository)
    {
        this.enrollmentRepository = enrollmentRepository;
    }
}
