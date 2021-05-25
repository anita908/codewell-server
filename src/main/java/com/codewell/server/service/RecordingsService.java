package com.codewell.server.service;

import com.codewell.server.dto.RecordingDto;

import java.util.List;

public interface RecordingsService
{
    List<RecordingDto> getRecordingsForSession(final String userId, final Integer sessionId);
}
