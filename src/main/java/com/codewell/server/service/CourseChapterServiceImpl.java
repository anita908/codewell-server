package com.codewell.server.service;

import com.codewell.server.dto.ChapterDto;
import com.codewell.server.persistence.entity.ChapterEntity;
import com.codewell.server.persistence.repository.ChapterRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class CourseChapterServiceImpl implements CourseChapterService
{
    private final ChapterRepository chapterRepository;

    @Inject
    public CourseChapterServiceImpl(final ChapterRepository chapterRepository)
    {
        this.chapterRepository = chapterRepository;

    }

    @Override
    public List<ChapterDto> getChaptersForCourse(final int courseId)
    {
        return chapterRepository.selectByCourseId(courseId)
            .stream()
            .map(this::mapToChapterDto)
            .collect(Collectors.toList());
    }

    private ChapterDto mapToChapterDto(final ChapterEntity chapterEntity)
    {
        final ChapterDto chapterDto = new ChapterDto();
        chapterDto.setId(chapterEntity.getId());
        chapterDto.setCourseId(chapterEntity.getCourseId());
        chapterDto.setChapterNo(chapterEntity.getChapterNo());
        chapterDto.setName(chapterEntity.getName());
        chapterDto.setSlidesLink(chapterEntity.getSlidesLink());
        return chapterDto;
    }
}
