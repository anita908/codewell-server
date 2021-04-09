package com.codewell.server.service;

import com.codewell.server.dto.CourseDto;
import com.codewell.server.dto.SessionDto;
import com.codewell.server.persistence.entity.SessionEntity;
import com.codewell.server.persistence.repository.SessionRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class SessionServiceImpl implements SessionService
{
    private final SessionRepository sessionRepository;

    @Inject
    public SessionServiceImpl(final SessionRepository sessionRepository)
    {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<SessionDto> getSessionsTaughtByAdmin(final String adminUserId)
    {
        return sessionRepository.selectByTeacherId(adminUserId)
            .stream()
            .map(this::mapToSessionDto)
            .collect(Collectors.toList());
    }

    private SessionDto mapToSessionDto(final SessionEntity sessionEntity)
    {
        final SessionDto sessionDto = new SessionDto();
        sessionDto.setId(sessionEntity.getId());
        final CourseDto courseDto = new CourseDto();
        courseDto.setId(sessionEntity.getCourse().getId());
        courseDto.setName(sessionEntity.getCourse().getName());
        courseDto.setPrice(sessionEntity.getCourse().getPrice());
        courseDto.setAgeLower(sessionEntity.getCourse().getAgeLower());
        courseDto.setAgeUpper(sessionEntity.getCourse().getAgeUpper());
        sessionDto.setCourse(courseDto);
        sessionDto.setBeginDate(sessionEntity.getBeginDate());
        sessionDto.setEndDate(sessionEntity.getEndDate());
        return sessionDto;
    }
}
