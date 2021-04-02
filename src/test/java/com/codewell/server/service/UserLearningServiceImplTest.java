package com.codewell.server.service;

import com.codewell.server.dto.*;
import com.codewell.server.persistence.entity.*;
import com.codewell.server.persistence.repository.ActivityRepository;
import com.codewell.server.persistence.repository.EnrollmentRepository;
import com.codewell.server.persistence.repository.GradeRepository;
import com.codewell.server.persistence.repository.HomeworkRepository;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserLearningServiceImplTest
{
    @Mock
    private UserService userService;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private HomeworkRepository homeworkRepository;
    @Mock
    private GradeRepository gradeRepository;
    @InjectMocks
    private UserLearningServiceImpl target;

    private UserDto userDto;
    private List<EnrollmentEntity> enrollments;
    private List<ActivityEntity> activities_1;
    private List<ActivityEntity> activities_2;
    private List<HomeworkEntity> homeworks_1;
    private List<HomeworkEntity> homeworks_2;
    private List<GradeEntity> grades_1;
    private List<GradeEntity> grades_2;
    private AtomicInteger sessionId = new AtomicInteger(1);
    private AtomicInteger courseId = new AtomicInteger(1);
    private AtomicInteger chapterId = new AtomicInteger(1);
    private AtomicInteger activityId = new AtomicInteger(1);
    private AtomicInteger homeworkId = new AtomicInteger(1);
    private AtomicInteger gradeId = new AtomicInteger(1);

    @BeforeEach
    void setup()
    {
        userDto = this.createNewUser();
        enrollments = this.createEnrollments();
        activities_1 = this.createActivities(enrollments.get(0));
        activities_2 = this.createActivities(enrollments.get(1));
        homeworks_1 = this.createHomeworks(enrollments.get(0));
        homeworks_2 = this.createHomeworks(enrollments.get(1));
        grades_1 = this.createGrades(enrollments.get(0), homeworks_1);
        grades_2 = this.createGrades(enrollments.get(1), homeworks_2);
    }

    @Test
    void test_getUserLearningModel_success()
    {
        UserLearningModel userLearningModel;
        try
        {
            when(userService.getUserById(anyString())).thenReturn(userDto);
            when(enrollmentRepository.selectByUserId(anyString())).thenReturn(enrollments);
            when(activityRepository.selectByCourseId(anyInt())).thenReturn(activities_1, activities_2);
            when(homeworkRepository.selectByCourseId(anyInt())).thenReturn(homeworks_1, homeworks_2);
            when(gradeRepository.selectBySessionAndUser(anyInt(), anyString())).thenReturn(grades_1, grades_2);

            userLearningModel = target.getUserLearningModel(userDto.getUserId());
        }
        catch (Exception exception)
        {
            userLearningModel = null;
        }

        assertNotNull(userLearningModel);
        assertEquals(userLearningModel.getUserData(), userDto);

        final List<EnrolledSessionProgressModel> enrolledSessions = userLearningModel.getEnrolledSessions();
        assertEquals(enrolledSessions.size(), 2);
        enrolledSessions.forEach(session ->
        {
            final List<ChapterProgressModel> chapters = session.getSessionProgressModel();
            assertEquals(chapters.size(), 12);
            chapters.forEach(chapter ->
            {
                assertEquals(chapter.getChapterName(), "Chapter " + chapter.getChapterNo());
                for (int i = 0; i < chapter.getActivities().size(); i++)
                {
                    final ActivityDto activity = chapter.getActivities().get(i);
                    assertEquals(activity.getCourseId(), session.getCourseId());
                    assertEquals(activity.getChapterNo(), chapter.getChapterNo());
                    assertEquals(activity.getName(), chapter.getChapterName() + " Activity " + (i + 1));
                }
                for (int i = 0; i < chapter.getHomeworkProgress().size(); i++)
                {
                    final HomeworkProgressModel homeworkProgressModel = chapter.getHomeworkProgress().get(i);
                    assertEquals(homeworkProgressModel.getHomeworkName(), chapter.getChapterName() + " Homework " + (i + 1));
                    assertEquals(homeworkProgressModel.getSubmitted(), "true");
                    assertEquals(homeworkProgressModel.getHomeworkScore(), 100.0);
                }
            });
        });
    }

    private List<EnrollmentEntity> createEnrollments()
    {
        final List<EnrollmentEntity> enrollments = new ArrayList<>();
        for (int i = 1; i <= 2; i++)
        {
            final EnrollmentEntity enrollment = new EnrollmentEntity();
            enrollment.setId(i);
            enrollment.setSession(this.createSession());
            enrollment.setUserId(userDto.getUserId());
            enrollment.setEnrollDate(OffsetDateTime.now());
            enrollment.setCurrentChapter(1);
            enrollment.setGraduated("false");
            enrollment.setOverallGrade(100.0);
            enrollment.setCreatedAt(OffsetDateTime.now());
            enrollment.setUpdatedAt(OffsetDateTime.now());
            enrollments.add(enrollment);
        }
        return enrollments;
    }

    private SessionEntity createSession()
    {
        final SessionEntity session = new SessionEntity();
        session.setId(sessionId.getAndIncrement());
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
        course.setId(courseId.getAndIncrement());
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
            chapter.setId(chapterId.getAndIncrement());
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

    private List<ActivityEntity> createActivities(final EnrollmentEntity enrollment)
    {
        final List<ActivityEntity> activities = new ArrayList<>();
        enrollment.getSession().getCourse().getChapters().forEach(chapter ->
        {
            for (int i = 1; i <= 2; i++)
            {
                final ActivityEntity activity = new ActivityEntity();
                activity.setId(activityId.getAndIncrement());
                activity.setCourseId(enrollment.getSession().getCourse().getId());
                activity.setChapterNo(chapter.getChapterNo());
                activity.setName("Chapter " + chapter.getChapterNo() + " Activity " + i);
                activity.setLink("link");
                activity.setCreatedAt(OffsetDateTime.now());
                activity.setUpdatedAt(OffsetDateTime.now());
                activities.add(activity);
            }

        });
        return activities;
    }

    private List<HomeworkEntity> createHomeworks(final EnrollmentEntity enrollment)
    {
        final List<HomeworkEntity> homeworks = new ArrayList<>();
        enrollment.getSession().getCourse().getChapters().forEach(chapter ->
        {
            for (int i = 1; i <= 2; i++)
            {
                final HomeworkEntity homework = new HomeworkEntity();
                homework.setId(homeworkId.getAndIncrement());
                homework.setCourseId(enrollment.getSession().getCourse().getId());
                homework.setChapterNo(chapter.getChapterNo());
                homework.setName("Chapter " + chapter.getChapterNo() + " Homework " + i);
                homework.setLink("link");
                homework.setCreatedAt(OffsetDateTime.now());
                homework.setUpdatedAt(OffsetDateTime.now());
                homeworks.add(homework);
            }
        });
        return homeworks;
    }

    private List<GradeEntity> createGrades(final EnrollmentEntity enrollment, final List<HomeworkEntity> homeworks)
    {
        final List<GradeEntity> grades = new ArrayList<>();
        homeworks.forEach(homework ->
        {
            final GradeEntity grade = new GradeEntity();
            grade.setId(gradeId.getAndIncrement());
            grade.setUserId(enrollment.getUserId());
            grade.setHomework(homework);
            grade.setScore(100.0);
            grade.setDueAt(OffsetDateTime.now().plusDays(7L));
            grade.setSubmitted("true");
            grade.setCreatedAt(OffsetDateTime.now());
            grade.setUpdatedAt(OffsetDateTime.now());
            grades.add(grade);
        });
        return grades;
    }

    private UserDto createNewUser()
    {
        final UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEmail("test@gmail.com");
        userDto.setFirstName("Test");
        userDto.setLastName("Name");
        userDto.setAge(8);
        userDto.setCity("Test City");
        userDto.setIsAdmin("false");

        return userDto;
    }
}
