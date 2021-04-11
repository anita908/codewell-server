package com.codewell.server.service;

import com.codewell.server.dto.EnrollmentDto;
import com.codewell.server.persistence.entity.EnrollmentEntity;
import com.codewell.server.persistence.entity.SessionEntity;
import com.codewell.server.persistence.repository.EnrollmentRepository;
import com.codewell.server.persistence.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class EnrollmentServiceImpl implements EnrollmentService
{
    private final GradesService gradesService;
    private final EnrollmentRepository enrollmentRepository;
    private final SessionRepository sessionRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    @Inject
    public EnrollmentServiceImpl(final GradesService gradesService,
                                 final EnrollmentRepository enrollmentRepository,
                                 final SessionRepository sessionRepository)
    {
        this.gradesService = gradesService;
        this.enrollmentRepository = enrollmentRepository;
        this.sessionRepository = sessionRepository;
    }

    public EnrollmentDto getEnrollmentById(final int enrollmentId)
    {
        LOGGER.info("Fetching enrollment record for id: {}", enrollmentId);
        return this.mapToEnrollmentDto(enrollmentRepository.select(enrollmentId));
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByUser(final String userId)
    {
        LOGGER.info("Fetching enrollment records by user id: {}", userId);
        return enrollmentRepository.selectByUserId(userId)
            .stream()
            .map(this::mapToEnrollmentDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsBySession(final Integer sessionId)
    {
        LOGGER.info("Fetching enrollment records by session id: {}", sessionId);
        return enrollmentRepository.selectBySessionId(sessionId)
            .stream()
            .map(this::mapToEnrollmentDto)
            .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDto getEnrollmentByUserAndSession(final String userId, final Integer sessionId)
    {
        LOGGER.info("Fetching enrollment records by user id:{} and session id: {}", userId, sessionId);
        return this.mapToEnrollmentDto(enrollmentRepository.selectByUserAndSession(userId, sessionId));
    }

    @Override
    public EnrollmentDto enrollStudentToSession(final String userId, final Integer sessionId)
    {
        final SessionEntity targetSession = sessionRepository.select(sessionId);
        if (targetSession == null)
        {
            throw new IllegalArgumentException(String.format("No session found for session id: %s", sessionId));
        }

        final EnrollmentEntity existingEnrollment = enrollmentRepository.selectByUserAndSession(userId, sessionId);
        if (existingEnrollment != null)
        {
            throw new IllegalArgumentException(String.format("User: %s is already enrolled in this session", userId));
        }

        final EnrollmentEntity newEnrollment = new EnrollmentEntity();
        final OffsetDateTime currentTime = OffsetDateTime.now();
        newEnrollment.setSession(targetSession);
        newEnrollment.setUserId(userId);
        newEnrollment.setEnrollDate(currentTime);
        newEnrollment.setCurrentChapter(1);
        newEnrollment.setGraduated("false");
        newEnrollment.setOverallGrade(100.0);
        newEnrollment.setCreatedAt(currentTime);
        newEnrollment.setUpdatedAt(currentTime);
        LOGGER.info("Inserting new enrollment into enrollment table: {}", newEnrollment);
        enrollmentRepository.insert(newEnrollment);

        gradesService.createDefaultGradesForUser(userId, targetSession);

        return this.getEnrollmentById(newEnrollment.getId());
    }

    @Override
    public EnrollmentDto updateEnrollmentRecord(final EnrollmentDto enrollmentDto)
    {
        Assert.notNull(enrollmentDto, "Enrollment dto cannot be null");
        Assert.notNull(enrollmentDto.getId(), "Enrollment id not provided");

        final EnrollmentEntity existingEnrollment = enrollmentRepository.select(enrollmentDto.getId());
        if (existingEnrollment == null)
        {
            throw new IllegalArgumentException(String.format("No enrollment record found for enrollment id: %s", enrollmentDto.getId()));
        }
        if (!existingEnrollment.getUserId().equals(enrollmentDto.getUserId()))
        {
            throw new IllegalArgumentException(String.format("User id provided does not match existing enrollment record: %s", enrollmentDto.getUserId()));
        }
        final SessionEntity targetSession = sessionRepository.select(enrollmentDto.getSessionId());
        if (targetSession == null)
        {
            throw new IllegalArgumentException(String.format("No session found for session id: %s", enrollmentDto.getSessionId()));
        }

        existingEnrollment.setSession(targetSession);
        existingEnrollment.setEnrollDate(enrollmentDto.getEnrollDate());
        existingEnrollment.setCurrentChapter(enrollmentDto.getCurrentChapter());
        existingEnrollment.setGraduated(enrollmentDto.getGraduated());
        existingEnrollment.setOverallGrade(enrollmentDto.getOverallGrade());
        existingEnrollment.setUpdatedAt(OffsetDateTime.now());
        return this.mapToEnrollmentDto(enrollmentRepository.update(existingEnrollment));
    }

    private EnrollmentDto mapToEnrollmentDto(final EnrollmentEntity enrollmentEntity)
    {
        if (enrollmentEntity == null)
        {
            return null;
        }
        final EnrollmentDto enrollmentDto = new EnrollmentDto();
        enrollmentDto.setId(enrollmentEntity.getId());
        enrollmentDto.setSessionId(enrollmentEntity.getSession().getId());
        enrollmentDto.setUserId(enrollmentEntity.getUserId());
        enrollmentDto.setEnrollDate(enrollmentEntity.getEnrollDate());
        enrollmentDto.setCurrentChapter(enrollmentEntity.getCurrentChapter());
        enrollmentDto.setOverallGrade(enrollmentEntity.getOverallGrade());
        enrollmentDto.setGraduated(enrollmentEntity.getGraduated());
        return enrollmentDto;
    }
}
