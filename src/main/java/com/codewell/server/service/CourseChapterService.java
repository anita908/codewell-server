package com.codewell.server.service;

import com.codewell.server.dto.ChapterDto;

import java.util.List;

public interface CourseChapterService
{
    List<ChapterDto> getChaptersForCourse(final int courseId);
}
