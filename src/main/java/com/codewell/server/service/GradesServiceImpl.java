package com.codewell.server.service;

import com.codewell.server.dto.GradeDto;
import com.codewell.server.persistence.entity.GradeEntity;
import com.codewell.server.persistence.repository.GradeRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class GradesServiceImpl implements GradesService
{
    private final GradeRepository gradeRepository;

    @Inject
    public GradesServiceImpl(final GradeRepository gradeRepository)
    {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public List<GradeDto> getAllGradesForUser(final String userId)
    {
        return gradeRepository.selectByUserId(userId)
            .stream()
            .map(this::mapToGradeDto)
            .collect(Collectors.toList());
    }

    private GradeDto mapToGradeDto(final GradeEntity gradeEntity)
    {
        final GradeDto gradeDto = new GradeDto();
        gradeDto.setId(gradeEntity.getId());
        gradeDto.setHomeworkId(gradeEntity.getHomework().getId());
        gradeDto.setHomeworkName(gradeEntity.getHomework().getName());
        gradeDto.setScore(gradeEntity.getScore());
        gradeDto.setDueDate(gradeEntity.getDueAt());
        gradeDto.setSubmitted(gradeEntity.getSubmitted());
        return gradeDto;
    }
}
