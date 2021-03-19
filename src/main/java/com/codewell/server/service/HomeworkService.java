package com.codewell.server.service;

import com.codewell.server.dto.HomeworkDto;
import com.codewell.server.dto.HomeworkVideoDto;

import java.util.List;

public interface HomeworkService
{
    List<HomeworkDto> getHomeworksForCourse(final int courseId);
    List<HomeworkDto> getHomeworksForCourseAndChapter(final int courseId, final int chapterNo);
    List<HomeworkVideoDto> getVideosForHomework(final int homeworkId);
    List<HomeworkVideoDto> getVideosForCourseAndChapter(final int courseId, final int chapterNo);
    List<HomeworkVideoDto> getVideosForCourse(final int courseId);
}
