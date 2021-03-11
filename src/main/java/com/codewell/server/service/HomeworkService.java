package com.codewell.server.service;

import com.codewell.server.dto.HomeworkVideoDto;

import java.util.List;

public interface HomeworkService
{
    List<HomeworkVideoDto> getVideosForHomework(final int homeworkId);
}
