package com.codewell.server.service;

import com.codewell.server.dto.ChapterDto;
import com.codewell.server.dto.CourseDto;
import com.codewell.server.dto.EnrollmentDto;
import com.codewell.server.dto.SessionDto;
import com.codewell.server.persistence.entity.ChapterEntity;
import com.codewell.server.persistence.entity.CourseEntity;
import com.codewell.server.persistence.entity.EnrollmentEntity;
import com.codewell.server.persistence.entity.SessionEntity;
import com.codewell.server.persistence.repository.EnrollmentRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<EnrollmentDto> getEnrollmentsForUser(final String userId)
    {
        return enrollmentRepository.selectByUserId(userId)
            .stream()
            .map(this::mapToEnrollmentDto)
            .collect(Collectors.toList());
    }

    private EnrollmentDto mapToEnrollmentDto(final EnrollmentEntity enrollmentEntity)
    {
        final EnrollmentDto enrollmentDto = new EnrollmentDto();
        enrollmentDto.setId(enrollmentEntity.getId());
        enrollmentDto.setSessionId(enrollmentEntity.getSession().getId());
        enrollmentDto.setCourseId(enrollmentEntity.getSession().getCourse().getId());
        enrollmentDto.setCourseName(enrollmentEntity.getSession().getCourse().getName());
        enrollmentDto.setEnrollDate(enrollmentEntity.getEnrollDate());
        enrollmentDto.setBeginDate(enrollmentEntity.getSession().getBeginDate());
        enrollmentDto.setEndDate(enrollmentEntity.getSession().getEndDate());
        enrollmentDto.setGraduated(enrollmentEntity.getGraduated());
        enrollmentDto.setOverallGrade(enrollmentEntity.getOverallGrade());
        return enrollmentDto;
    }
}
