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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
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
    private final S3Client s3Client;

    @Autowired
    private Environment env;

    @Inject
    public HomeworkServiceImpl(final HomeworkRepository homeworkRepository,
                               final HomeworkVideoRepository homeworkVideoRepository,
                               final GradeRepository gradeRepository,
                               final S3Client s3Client)
    {
        this.homeworkRepository = homeworkRepository;
        this.homeworkVideoRepository = homeworkVideoRepository;
        this.gradeRepository = gradeRepository;
        this.s3Client = s3Client;
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
            throw new IllegalArgumentException(String.format("No grade record found for userId: %s, sessionId: %s, and homeworkId: %s.", userId, sessionId, homeworkId));
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
    public void uploadHomework(final String userId, final Integer sessionId, final Integer homeworkId, final InputStream inputStream, final String fileType) throws IOException
    {
        GradeEntity grade = gradeRepository.selectByUserSessionAndHomeworkId(userId, sessionId, homeworkId);
        if (grade == null)
        {
            throw new IllegalArgumentException(String.format("No grade record found for userId: %s, sessionId: %s, and homeworkId: %s.", userId, sessionId, homeworkId));
        }
        else
        {
            final String fileName = String.format("%s-%s-%s.%s", userId, sessionId, homeworkId, fileType);
            final String url = this.saveHomeworkFileInS3(inputStream, fileName);

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

    private String saveHomeworkFileInS3(final InputStream inputStream, final String fileName) throws IOException
    {
        final String bucket = env.getProperty("homework.submission.bucket", "codewell-homework-submission");

        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build();
        final RequestBody requestBody = RequestBody.fromBytes(inputStream.readAllBytes());
        s3Client.putObject(putObjectRequest, requestBody);

        final GetUrlRequest getUrlRequest = GetUrlRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .build();
        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }
}
