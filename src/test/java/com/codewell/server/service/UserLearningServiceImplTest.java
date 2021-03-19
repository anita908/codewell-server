package com.codewell.server.service;

import com.codewell.server.persistence.repository.ActivityRepository;
import com.codewell.server.persistence.repository.EnrollmentRepository;
import com.codewell.server.persistence.repository.GradeRepository;
import com.codewell.server.persistence.repository.HomeworkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void test_getUserLearningModel_success()
    {
    }
}
