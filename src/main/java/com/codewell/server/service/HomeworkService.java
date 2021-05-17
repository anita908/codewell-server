package com.codewell.server.service;

import com.codewell.server.dto.HomeworkDto;
import com.codewell.server.dto.HomeworkVideoDto;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;

public interface HomeworkService
{
    List<HomeworkDto> getHomeworksForCourse(final int courseId);
    List<HomeworkDto> getHomeworksForCourseAndChapter(final int courseId, final int chapterNo);
    void uploadHomework(final String userId, final Integer sessionId, final Integer homeworkId, final String url);
    void uploadHomework(final String userId, final Integer sessionId, final Integer homeworkId, final InputStream inputStream, final String fileType) throws IOException;
    List<HomeworkVideoDto> getVideosForHomework(final int homeworkId);
    List<HomeworkVideoDto> getVideosForCourseAndChapter(final int courseId, final int chapterNo);
    List<HomeworkVideoDto> getVideosForCourse(final int courseId);
}
