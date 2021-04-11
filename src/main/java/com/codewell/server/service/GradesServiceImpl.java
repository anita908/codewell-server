package com.codewell.server.service;

import com.codewell.server.dto.GradeDto;
import com.codewell.server.persistence.entity.EnrollmentEntity;
import com.codewell.server.persistence.entity.GradeEntity;
import com.codewell.server.persistence.entity.HomeworkEntity;
import com.codewell.server.persistence.entity.SessionEntity;
import com.codewell.server.persistence.repository.EnrollmentRepository;
import com.codewell.server.persistence.repository.GradeRepository;
import com.codewell.server.persistence.repository.HomeworkRepository;
import com.codewell.server.util.DataValidator;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Named
@Singleton
public class GradesServiceImpl implements GradesService
{
    private final GradeRepository gradeRepository;
    private final HomeworkRepository homeworkRepository;
    private final EnrollmentRepository enrollmentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(GradesServiceImpl.class);

    @Inject
    public GradesServiceImpl(final GradeRepository gradeRepository,
                             final HomeworkRepository homeworkRepository,
                             final EnrollmentRepository enrollmentRepository)
    {
        this.gradeRepository = gradeRepository;
        this.homeworkRepository = homeworkRepository;
        this.enrollmentRepository = enrollmentRepository;
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

        final List<GradeEntity> originalGrades = gradeRepository.selectByUserAndSession(userId, sessionId);
        if (originalGrades.isEmpty())
        {
            throw new IllegalArgumentException("No grades exist for user and session");
        }
        final GradeEntity originalGrade = originalGrades.stream()
            .filter(grade -> grade.getHomework().getId().equals(gradeDto.getHomeworkId()))
            .findFirst()
            .orElse(null);
        if (originalGrade == null)
        {
            throw new IllegalArgumentException("Could not find grade record with given query parameters");
        }
        final EnrollmentEntity enrollment = enrollmentRepository.selectByUserAndSession(userId, sessionId);
        if (enrollment == null)
        {
            throw new IllegalArgumentException(String.format("No enrollment record found for user id: %s and session id: %s", userId, sessionId));
        }

        originalGrade.setScore(gradeDto.getScore());
        originalGrade.setDueAt(gradeDto.getDueDate());
        originalGrade.setSubmitted(gradeDto.getSubmitted());
        originalGrade.setUpdatedAt(OffsetDateTime.now());
        final GradeEntity newGrade = gradeRepository.update(originalGrade);

        enrollment.setOverallGrade(this.calculateAverageGrade(originalGrades));
        enrollment.setUpdatedAt(OffsetDateTime.now());
        enrollmentRepository.update(enrollment);

        return this.mapToGradeDto(newGrade);
    }

    @Override
    public List<GradeDto> bulkModifyGrades(final String userId, final Integer sessionId, final List<GradeDto> gradeDtos) throws InterruptedException
    {
        Assert.hasText(userId, "User id not provided");
        Assert.notNull(sessionId, "Session id not provided");
        Assert.notNull(gradeDtos, "Grade dto list cannot be null");
        gradeDtos.forEach(gradeDto ->
        {
            Assert.notNull(gradeDto.getHomeworkId(), "Homework id cannot be null");
            Assert.hasText(gradeDto.getSubmitted(), "Submitted flag cannot be null");
            Assert.isTrue(DataValidator.isValidBoolean(gradeDto.getSubmitted()), "Submitted flag is not a valid boolean value");
        });
        final List<GradeEntity> originalGrades = gradeRepository.selectByUserAndSession(userId, sessionId);
        final ExecutorService executor = Executors.newFixedThreadPool(5);
        AtomicBoolean updated = new AtomicBoolean(false);
        gradeDtos.forEach(gradeDto ->
        {
            originalGrades.stream()
                .filter(gradeEntity -> gradeEntity.getId().equals(gradeDto.getId()))
                .findFirst()
                .ifPresent(gradeEntity ->
                {
                    if (!this.containEqualPayloads(gradeDto, gradeEntity))
                    {
                        gradeEntity.setScore(gradeDto.getScore());
                        gradeEntity.setDueAt(gradeDto.getDueDate());
                        gradeEntity.setSubmitted(gradeDto.getSubmitted());
                        gradeEntity.setUpdatedAt(OffsetDateTime.now());
                        executor.submit(() -> gradeRepository.update(gradeEntity));
                        updated.set(true);
                    }
                });
        });
        executor.shutdown();
        if (!executor.awaitTermination(20, TimeUnit.SECONDS))
        {
            throw new InterruptedException("Bulk grades update timed out");
        }

        List<GradeEntity> newGrades = originalGrades;
        if (updated.get())
        {
            final EnrollmentEntity enrollment = enrollmentRepository.selectByUserAndSession(userId, sessionId);
            if (enrollment == null)
            {
                throw new IllegalArgumentException(String.format("No enrollment record found for user id: %s and session id: %s", userId, sessionId));
            }
            newGrades = gradeRepository.selectByUserAndSession(userId, sessionId);
            enrollment.setOverallGrade(this.calculateAverageGrade(newGrades));
            enrollment.setUpdatedAt(OffsetDateTime.now());
            enrollmentRepository.update(enrollment);
        }

        return newGrades.stream()
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

    private boolean containEqualPayloads(final GradeDto gradeDto, final GradeEntity gradeEntity)
    {
        if (!gradeEntity.getId().equals(gradeDto.getId()))
        {
            return false;
        }
        if (gradeEntity.getScore() != null && gradeDto.getScore() != null)
        {
            if (!gradeEntity.getScore().equals(gradeDto.getScore()))
            {
                return false;
            }
        }
        else if (gradeEntity.getScore() == null && gradeDto.getScore() != null)
        {
            return false;
        }
        else if (gradeEntity.getScore() != null && gradeDto.getScore() == null)
        {
            return false;
        }
        if (gradeEntity.getDueAt() != null && gradeDto.getDueDate() != null)
        {
            if (!gradeEntity.getDueAt().isEqual(gradeDto.getDueDate()))
            {
                return false;
            }
        }
        else if (gradeEntity.getDueAt() == null && gradeDto.getDueDate() != null)
        {
            return false;
        }
        else if (gradeEntity.getDueAt() != null && gradeDto.getDueDate() == null)
        {
            return false;
        }
        return gradeEntity.getSubmitted().equals(gradeDto.getSubmitted());
    }

    private double calculateAverageGrade(final List<GradeEntity> grades)
    {
        int count = 0;
        double total = 0;
        for (GradeEntity grade : grades)
        {
            if (grade.getScore() != null)
            {
                total += grade.getScore();
                count++;
            }
        }
        return count != 0 ? total / count : 100.0;
    }
}
