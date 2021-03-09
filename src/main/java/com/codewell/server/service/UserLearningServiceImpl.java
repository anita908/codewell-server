package com.codewell.server.service;

import com.codewell.server.dto.UserLearningModel;
import com.codewell.server.persistence.repository.GradeRepository;
import com.codewell.server.persistence.repository.HomeworkRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Singleton;

@Component
@Singleton
public class UserLearningServiceImpl implements UserLearningService
{
    private final UserService userService;
    private final EnrollmentService enrollmentService;
    private final HomeworkRepository homeworkRepository;
    private final GradeRepository gradeRepository;

    @Inject
    public UserLearningServiceImpl(final UserService userService,
                                   final EnrollmentService enrollmentService,
                                   final HomeworkRepository homeworkRepository,
                                   final GradeRepository gradeRepository)
    {
        this.userService = userService;
        this.enrollmentService = enrollmentService;
        this.homeworkRepository = homeworkRepository;
        this.gradeRepository = gradeRepository;
    }

    @Override
    public UserLearningModel getUserLearningModel(final String userId)
    {
        final UserLearningModel userLearningModel = new UserLearningModel();
        userLearningModel.setUserData(userService.getUserById(userId));
        userLearningModel.setEnrolledSessions(enrollmentService.getEnrollmentsForUser(userId));
        userLearningModel.getEnrolledSessions().forEach(session ->
        {
            userLearningModel.coalesceAssignmentsAndGrades(session,
                homeworkRepository.selectByCourseId(session.getCourseId()),
                gradeRepository.selectBySessionAndUser(session.getSessionId(), userId));
        });
        return userLearningModel;
    }
}
