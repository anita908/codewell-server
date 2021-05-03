package com.codewell.server.service;

import com.codewell.server.dto.HomeworkDto;
import com.codewell.server.dto.HomeworkVideoDto;
import com.codewell.server.persistence.entity.GradeEntity;
import com.codewell.server.persistence.entity.HomeworkEntity;
import com.codewell.server.persistence.entity.HomeworkVideoEntity;
import com.codewell.server.persistence.repository.GradeRepository;
import com.codewell.server.persistence.repository.HomeworkRepository;
import com.codewell.server.persistence.repository.HomeworkVideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class HomeworkServiceImpl implements HomeworkService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeworkServiceImpl.class);

    private final HomeworkRepository homeworkRepository;
    private final HomeworkVideoRepository homeworkVideoRepository;
    private final GradeRepository gradeRepository;

    @Inject
    public HomeworkServiceImpl(final HomeworkRepository homeworkRepository,
                               final HomeworkVideoRepository homeworkVideoRepository,
                               final GradeRepository gradeRepository)
    {
        this.homeworkRepository = homeworkRepository;
        this.homeworkVideoRepository = homeworkVideoRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    public List<HomeworkDto> getHomeworksForCourse(final int courseId)
    {
        return homeworkRepository.selectByCourseId(courseId)
            .stream()
            .map(this::mapToHomeworkDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<HomeworkDto> getHomeworksForCourseAndChapter(final int courseId, final int chapterNo)
    {
        return homeworkRepository.selectByCourseIdAndChapterNo(courseId, chapterNo)
            .stream()
            .map(this::mapToHomeworkDto)
            .collect(Collectors.toList());
    }

    @Override
    public void uploadHomework(final String userId, final Integer sessionId, final Integer homeworkId, final String url)
    {
        GradeEntity grade = gradeRepository.selectByUserSessionAndHomeworkId(userId, sessionId, homeworkId);
        if (grade == null)
        {
            LOGGER.info("No grade record found for userId: {}, sessionId: {}, and homeworkId: {}. Attempting insertion of new grade record.", userId, sessionId, homeworkId);
            final HomeworkEntity homework = homeworkRepository.select(homeworkId);
            if (homework == null)
            {
                throw new IllegalArgumentException(String.format("No homework found for homework id: %s", homeworkId));
            }
            grade = new GradeEntity();
            grade.setSessionId(sessionId);
            grade.setUserId(userId);
            grade.setSubmissionUrl(url);
            grade.setScore(null);
            grade.setFeedback(null);
            grade.setDueAt(null);
            grade.setSubmitted("true");
            final OffsetDateTime currentTime = OffsetDateTime.now();
            grade.setCreatedAt(currentTime);
            grade.setUpdatedAt(currentTime);
            gradeRepository.insert(grade);
            LOGGER.info("Inserted new grade record with homeworkId: {} into grades table", homeworkId);
        }
        else
        {
            grade.setSubmissionUrl(url);
            grade.setSubmitted("true");
            grade.setUpdatedAt(OffsetDateTime.now());
            gradeRepository.update(grade);
        }

    }

    @Override
    public List<HomeworkVideoDto> getVideosForHomework(final int homeworkId)
    {
        return homeworkVideoRepository.selectByHomeworkId(homeworkId)
            .stream()
            .map(this::mapToHomeworkVideoDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<HomeworkVideoDto> getVideosForCourseAndChapter(final int courseId, final int chapterNo)
    {
        List<Integer> homeworkIds = this.getHomeworksForCourseAndChapter(courseId, chapterNo)
            .stream()
            .map(HomeworkDto::getId)
            .collect(Collectors.toList());
        return homeworkVideoRepository.selectByHomeworkIds(homeworkIds)
            .stream()
            .map(this::mapToHomeworkVideoDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<HomeworkVideoDto> getVideosForCourse(final int courseId)
    {
        final List<Integer> homeworkIds = this.getHomeworksForCourse(courseId)
            .stream()
            .map(HomeworkDto::getId)
            .collect(Collectors.toList());
        return homeworkVideoRepository.selectByHomeworkIds(homeworkIds)
            .stream()
            .map(this::mapToHomeworkVideoDto)
            .collect(Collectors.toList());
    }

    private HomeworkDto mapToHomeworkDto(final HomeworkEntity entity)
    {
        final HomeworkDto dto = new HomeworkDto();
        dto.setId(entity.getId());
        dto.setCourseId(entity.getCourseId());
        dto.setChapterNo(entity.getChapterNo());
        dto.setHomeworkName(entity.getName());
        dto.setLink(entity.getLink());
        return dto;
    }

    private HomeworkVideoDto mapToHomeworkVideoDto(final HomeworkVideoEntity entity)
    {
        final HomeworkVideoDto dto = new HomeworkVideoDto();
        dto.setId(entity.getId());
        dto.setHomeworkId(entity.getHomeworkId());
        dto.setName(entity.getName());
        dto.setStorageUrl(entity.getStorageUrl());
        return dto;
    }
}
