package com.codewell.server.service;

import com.codewell.server.dto.HomeworkDto;
import com.codewell.server.dto.HomeworkVideoDto;
import com.codewell.server.persistence.entity.HomeworkEntity;
import com.codewell.server.persistence.entity.HomeworkVideoEntity;
import com.codewell.server.persistence.repository.HomeworkRepository;
import com.codewell.server.persistence.repository.HomeworkVideoRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class HomeworkServiceImpl implements HomeworkService
{
    private final HomeworkRepository homeworkRepository;
    private final HomeworkVideoRepository homeworkVideoRepository;

    @Inject
    public HomeworkServiceImpl(final HomeworkRepository homeworkRepository, final HomeworkVideoRepository homeworkVideoRepository)
    {
        this.homeworkRepository = homeworkRepository;
        this.homeworkVideoRepository = homeworkVideoRepository;
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
