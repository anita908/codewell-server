package com.codewell.server.service;

import com.codewell.server.dto.RecordingDto;
import com.codewell.server.persistence.entity.EnrollmentEntity;
import com.codewell.server.persistence.entity.RecordingEntity;
import com.codewell.server.persistence.repository.EnrollmentRepository;
import com.codewell.server.persistence.repository.RecordingRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
public class RecordingsServiceImpl implements RecordingsService
{
    private final RecordingRepository recordingRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Inject
    public RecordingsServiceImpl(final RecordingRepository recordingRepository,
                                 final EnrollmentRepository enrollmentRepository)
    {
        this.recordingRepository = recordingRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<RecordingDto> getRecordingsForSession(final String userId, final Integer sessionId)
    {
        final List<EnrollmentEntity> enrollments = enrollmentRepository.selectByUserId(userId);
        if (enrollments.stream().noneMatch(enrollment -> enrollment.getSession().getId().equals(sessionId)))
        {
            throw new IllegalArgumentException(String.format("User: %s is not enrolled in session: %s", userId, sessionId));
        }

        return recordingRepository.selectBySessionId(sessionId)
            .stream()
            .map(this::mapToRecordingDto)
            .collect(Collectors.toList());
    }

    private RecordingDto mapToRecordingDto(final RecordingEntity recordingEntity)
    {
        final RecordingDto recordingDto = new RecordingDto();
        recordingDto.setId(recordingEntity.getId());
        recordingDto.setSessionId(recordingEntity.getSessionId());
        recordingDto.setChapterNo(recordingEntity.getChapterNo());
        recordingDto.setUrl(recordingEntity.getUrl());
        return recordingDto;
    }
}
