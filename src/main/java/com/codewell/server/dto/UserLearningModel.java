package com.codewell.server.dto;

import com.codewell.server.persistence.entity.GradeEntity;
import com.codewell.server.persistence.entity.HomeworkEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLearningModel
{
    private UserDto userData;
    private List<EnrollmentDto> enrolledSessions = new ArrayList<>();

    public void coalesceAssignmentsAndGrades(final EnrollmentDto enrollment, final List<HomeworkEntity> homeworkEntities, final List<GradeEntity> gradeEntities)
    {
        final Map<Integer, GradeDto> gradeMap = new HashMap<>();
        final List<GradeDto> grades = new ArrayList<>();
        homeworkEntities.forEach(homeworkEntity -> {
            final GradeDto grade = new GradeDto();
            grade.setHomeworkId(homeworkEntity.getId());
            grade.setHomeworkName(homeworkEntity.getName());
            grades.add(grade);
            gradeMap.put(grade.getHomeworkId(), grade);
        });
        gradeEntities.forEach(gradeEntity ->
        {
            final GradeDto grade = gradeMap.get(gradeEntity.getHomeworkId());
            grade.setId(gradeEntity.getId());
            grade.setScore(gradeEntity.getScore());
            grade.setDueDate(gradeEntity.getDueAt());
            grade.setSubmitted(gradeEntity.getSubmitted());
        });
        enrollment.setGrades(grades);
    }

    public UserDto getUserData()
    {
        return userData;
    }

    public void setUserData(UserDto userData)
    {
        this.userData = userData;
    }

    public List<EnrollmentDto> getEnrolledSessions()
    {
        return enrolledSessions;
    }

    public void setEnrolledSessions(List<EnrollmentDto> enrolledSessions)
    {
        this.enrolledSessions = enrolledSessions;
    }
}