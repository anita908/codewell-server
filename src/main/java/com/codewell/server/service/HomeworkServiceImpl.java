package com.codewell.server.service;

import com.codewell.server.dto.HomeworkVideoDto;
import com.codewell.server.persistence.entity.HomeworkVideoEntity;
import com.codewell.server.persistence.repository.HomeworkVideoRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Singleton
public class HomeworkServiceImpl implements HomeworkService
{
    private final HomeworkVideoRepository homeworkVideoRepository;

    @Inject
    public HomeworkServiceImpl(final HomeworkVideoRepository homeworkVideoRepository)
    {
        this.homeworkVideoRepository = homeworkVideoRepository;
    }

    @Override
    public List<HomeworkVideoDto> getVideosForHomework(final int homeworkId)
    {
        return homeworkVideoRepository.selectByHomeworkId(homeworkId)
            .stream()
            .map(this::mapToHomeworkVideoDto)
            .collect(Collectors.toList());
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
