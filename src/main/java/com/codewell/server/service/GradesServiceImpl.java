package com.codewell.server.service;

import com.codewell.server.dto.GradeDto;
import com.codewell.server.persistence.entity.GradeEntity;
import com.codewell.server.persistence.entity.HomeworkEntity;
import com.codewell.server.persistence.entity.SessionEntity;
import com.codewell.server.persistence.repository.GradeRepository;
import com.codewell.server.persistence.repository.HomeworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Named
@Singleton
public class GradesServiceImpl implements GradesService
{
    private final GradeRepository gradeRepository;
    private final HomeworkRepository homeworkRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(GradesServiceImpl.class);

    @Inject
    public GradesServiceImpl(final GradeRepository gradeRepository, final HomeworkRepository homeworkRepository)
    {
        this.gradeRepository = gradeRepository;
        this.homeworkRepository = homeworkRepository;
    }

    @Override
    public void createDefaultGradesForUser(final String userId, final SessionEntity session)
    {
        Assert.hasText(userId, "User id not provided");
        Assert.notNull(session, "Session is null");

        final List<HomeworkEntity> homeworks = homeworkRepository.selectByCourseId(session.getCourse().getId());
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        homeworks.forEach(homework ->
        {
            final GradeEntity newGrade = new GradeEntity();
            final OffsetDateTime currentTime = OffsetDateTime.now();
            newGrade.setSessionId(session.getId());
            newGrade.setHomework(homework);
            newGrade.setUserId(userId);
            newGrade.setScore(null);
            newGrade.setDueAt(null);
            newGrade.setSubmitted("false");
            newGrade.setCreatedAt(currentTime);
            newGrade.setUpdatedAt(currentTime);
            LOGGER.info("Inserting new grade into grades table: {}", newGrade.toString());
            executor.submit(() -> gradeRepository.insert(newGrade));
        });
        executor.shutdown();
    }

    @Override
    public List<GradeDto> getGradesForUser(final String userId, final Integer sessionId)
    {
        Assert.hasText(userId, "User id not provided");
        List<GradeEntity> grades;
        if (sessionId == null)
        {
            grades = gradeRepository.selectByUserId(userId);
        }
        else
        {
            grades = gradeRepository.selectByUserAndSession(userId, sessionId);
        }
        return grades.stream()
            .map(this::mapToGradeDto)
            .collect(Collectors.toList());
    }

    @Override
    public GradeDto modifyGrade(final String userId, final Integer sessionId, final GradeDto gradeDto)
    {
        Assert.hasText(userId, "User id not provided");
        Assert.notNull(sessionId, "Session id not provided");
        Assert.notNull(gradeDto, "Grade dto cannot be null");
        Assert.notNull(gradeDto.getHomeworkId(), "Homework id cannot be null");
        Assert.hasText(gradeDto.getSubmitted(), "Submitted flag cannot be null");

        final GradeEntity originalGrade = gradeRepository.selectByUserSessionAndHomeworkId(userId, sessionId, gradeDto.getHomeworkId());
        if (originalGrade == null)
        {
            throw new IllegalArgumentException("Could not find grade record for given query parameters");
        }

        originalGrade.setScore(gradeDto.getScore());
        originalGrade.setDueAt(gradeDto.getDueDate());
        originalGrade.setSubmitted(gradeDto.getSubmitted());

        return mapToGradeDto(gradeRepository.update(originalGrade));
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
