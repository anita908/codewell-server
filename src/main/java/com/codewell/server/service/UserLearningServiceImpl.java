package com.codewell.server.service;

import com.codewell.server.dto.ChapterProgressModel;
import com.codewell.server.dto.EnrolledSessionProgressModel;
import com.codewell.server.dto.UserDto;
import com.codewell.server.dto.UserLearningModel;
import com.codewell.server.persistence.entity.*;
import com.codewell.server.persistence.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Named
@Singleton
public class UserLearningServiceImpl implements UserLearningService
{
    private final UserService userService;
    private final EnrollmentRepository enrollmentRepository;
    private final ActivityRepository activityRepository;
    private final HomeworkRepository homeworkRepository;
    private final GradeRepository gradeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLearningServiceImpl.class);

    @Inject
    public UserLearningServiceImpl(final UserService userService,
                                   final EnrollmentRepository enrollmentRepository,
                                   final ActivityRepository activityRepository,
                                   final HomeworkRepository homeworkRepository,
                                   final GradeRepository gradeRepository)
    {
        this.userService = userService;
        this.enrollmentRepository = enrollmentRepository;
        this.activityRepository = activityRepository;
        this.homeworkRepository = homeworkRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    public UserLearningModel getUserLearningModel(final String userId) throws Exception
    {
        try
        {
            final UserLearningModel userLearningModel = new UserLearningModel();

            final ExecutorService executor = Executors.newFixedThreadPool(5);
            Future<UserDto> userDtoFuture = executor.submit(() -> userService.getUserById(userId));
            Future<List<EnrollmentEntity>> enrollmentsFuture = executor.submit(() -> enrollmentRepository.selectByUserId(userId));

            userLearningModel.setUserData(userDtoFuture.get());
            userLearningModel.setEnrolledSessions(enrollmentsFuture.get()
                .stream()
                .map(this::mapToEnrolledSessionProgressModel)
                .collect(Collectors.toList()));

            for (final EnrolledSessionProgressModel session : userLearningModel.getEnrolledSessions())
            {
                Future<List<ActivityEntity>> activitiesFuture = executor.submit(() -> activityRepository.selectByCourseId(session.getCourseId()));
                Future<List<HomeworkEntity>> homeworkFuture = executor.submit(() -> homeworkRepository.selectByCourseId(session.getCourseId()));
                Future<List<GradeEntity>> gradesFuture = executor.submit(() -> gradeRepository.selectBySessionAndUser(session.getSessionId(), userId));
                userLearningModel.coalesceAssignmentsAndGrades(session, activitiesFuture.get(), homeworkFuture.get(), gradesFuture.get());
            }
            executor.shutdown();

            return userLearningModel;
        }
        catch (Exception exception)
        {
            LOGGER.error("Encountered exception while creating user learning model: {}", exception.getMessage());
            throw new Exception(exception);
        }
    }

    private EnrolledSessionProgressModel mapToEnrolledSessionProgressModel(final EnrollmentEntity enrollmentEntity)
    {
        final EnrolledSessionProgressModel enrolledSessionProgressModel = new EnrolledSessionProgressModel();
        enrolledSessionProgressModel.setEnrollmentId(enrollmentEntity.getId());
        enrolledSessionProgressModel.setSessionId(enrollmentEntity.getSession().getId());
        enrolledSessionProgressModel.setCourseId(enrollmentEntity.getSession().getCourse().getId());
        enrolledSessionProgressModel.setCourseName(enrollmentEntity.getSession().getCourse().getName());
        enrolledSessionProgressModel.setEnrollDate(enrollmentEntity.getEnrollDate());
        enrolledSessionProgressModel.setBeginDate(enrollmentEntity.getSession().getBeginDate());
        enrolledSessionProgressModel.setEndDate(enrollmentEntity.getSession().getEndDate());
        enrolledSessionProgressModel.setCurrentChapter(enrollmentEntity.getCurrentChapter());
        enrolledSessionProgressModel.setGraduated(enrollmentEntity.getGraduated());
        enrolledSessionProgressModel.setOverallGrade(enrollmentEntity.getOverallGrade());
        enrolledSessionProgressModel.setSessionProgressModel(enrollmentEntity.getSession()
            .getCourse()
            .getChapters()
            .stream()
            .map(chapter -> ChapterProgressModel.newBuilder()
                    .chapterId(chapter.getId())
                    .chapterNo(chapter.getChapterNo())
                    .chapterName(chapter.getName())
                    .slidesLink(chapter.getSlidesLink())
                    .build()
            ).collect(Collectors.toList()));
        return enrolledSessionProgressModel;
    }
}
