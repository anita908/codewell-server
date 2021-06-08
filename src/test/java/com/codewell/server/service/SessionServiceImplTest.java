package com.codewell.server.service;

import com.codewell.server.dto.SessionDto;
import com.codewell.server.persistence.entity.*;
import com.codewell.server.persistence.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class SessionServiceImplTest {

    @Mock
    private SessionRepository sessionRepository;
    @InjectMocks
    private SessionServiceImpl target;

    @Test
    void test_getSessionsTaughtByAdmin_success() {

        final SessionEntity sessionEntity = this.createSession();
        when(sessionRepository.selectByTeacherId(anyString())).thenReturn(Collections.singletonList(sessionEntity));

        List<SessionDto> sessions = target.getSessionsTaughtByAdmin(anyString());
        sessions.forEach(session -> this.assertEqualDtoAndEntity(session, sessionEntity));
    }

    @Test
    void test_getSessionsTaughtByAdmin_noSessions() {

        when(sessionRepository.selectByTeacherId(anyString())).thenReturn(Collections.emptyList());

        List<SessionDto> sessions = target.getSessionsTaughtByAdmin(UUID.randomUUID().toString());
        assertEquals(sessions.size(), 0);
    }

    private void assertEqualDtoAndEntity(final SessionDto sessionDto, final SessionEntity sessionEntity)
    {
        assertEquals(sessionDto.getId(), sessionEntity.getId());
        assertEquals(sessionDto.getBeginDate(), sessionEntity.getBeginDate());
        assertEquals(sessionDto.getEndDate(), sessionEntity.getEndDate());
    }

    private SessionEntity createSession()
    {
        final SessionEntity session = new SessionEntity();
        session.setId(1);
        session.setCourse(this.createCourse());
        session.setBeginDate(OffsetDateTime.now().minusDays(7L));
        session.setEndDate(OffsetDateTime.now().plusDays(7L));
        session.setCreatedAt(OffsetDateTime.now());
        session.setUpdatedAt(OffsetDateTime.now());
        return session;
    }

    private CourseEntity createCourse()
    {
        final CourseEntity course = new CourseEntity();
        course.setId(1);
        course.setName("Test Course Name");
        course.setAgeLower(8);
        course.setAgeUpper(12);
        course.setPrice(200.0);
        course.setChapters(this.createChapters(course));
        course.setCreatedAt(OffsetDateTime.now());
        course.setUpdatedAt(OffsetDateTime.now());
        return course;
    }

    private List<ChapterEntity> createChapters(final CourseEntity course)
    {
        final List<ChapterEntity> chapters = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
        {
            final ChapterEntity chapter = new ChapterEntity();
            chapter.setId(i);
            chapter.setCourseId(course.getId());
            chapter.setChapterNo(i);
            chapter.setName("Chapter " + i);
            chapter.setSlidesLink("link");
            chapter.setCreatedAt(OffsetDateTime.now());
            chapter.setUpdatedAt(OffsetDateTime.now());
            chapters.add(chapter);
        }
        return chapters;
    }
}