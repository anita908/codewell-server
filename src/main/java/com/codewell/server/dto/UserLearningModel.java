package com.codewell.server.dto;

import com.codewell.server.persistence.entity.ActivityEntity;
import com.codewell.server.persistence.entity.GradeEntity;
import com.codewell.server.persistence.entity.HomeworkEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLearningModel
{
    private UserDto userData;
    private List<EnrolledSessionProgressModel> enrolledSessions = new ArrayList<>();

    public void coalesceAssignmentsAndGrades(final EnrolledSessionProgressModel enrolledSession,
                                             final List<ActivityEntity> activityEntities,
                                             final List<HomeworkEntity> homeworkEntities,
                                             final List<GradeEntity> gradeEntities)
    {
        final Map<Integer, ChapterProgressModel> chapterNoToProgressMap = new HashMap<>();
        final Map<Integer, HomeworkProgressModel> homeworkIdToProgressMap = new HashMap<>();
        enrolledSession.getSessionProgressModel().forEach(chapterProgressModel -> chapterNoToProgressMap.put(chapterProgressModel.getChapterNo(), chapterProgressModel));


        activityEntities.forEach(activityEntity ->
        {
            final ChapterProgressModel chapterProgress = chapterNoToProgressMap.get(activityEntity.getChapterNo());
            final ActivityDto activity = new ActivityDto();

            activity.setId(activityEntity.getId());
            activity.setCourseId(activityEntity.getCourseId());
            activity.setChapterNo(activityEntity.getChapterNo());
            activity.setName(activityEntity.getName());
            activity.setLink(activityEntity.getLink());

            chapterProgress.getActivities().add(activity);
        });

        homeworkEntities.forEach(homeworkEntity -> {
            final ChapterProgressModel chapterProgress = chapterNoToProgressMap.get(homeworkEntity.getChapterNo());
            final HomeworkProgressModel homeworkProgress = new HomeworkProgressModel();

            homeworkProgress.setHomeworkId(homeworkEntity.getId());
            homeworkProgress.setHomeworkName(homeworkEntity.getName());
            homeworkProgress.setHomeworkLink(homeworkEntity.getLink());

            chapterProgress.getHomeworkProgress().add(homeworkProgress);
            homeworkIdToProgressMap.put(homeworkEntity.getId(), homeworkProgress);
        });

        gradeEntities.forEach(gradeEntity ->
        {
            final HomeworkProgressModel homeworkProgress = homeworkIdToProgressMap.get(gradeEntity.getHomework().getId());
            homeworkProgress.setHomeworkScore(gradeEntity.getScore());
            homeworkProgress.setSubmitted(gradeEntity.getSubmitted());
            homeworkProgress.setSubmissionUrl(gradeEntity.getSubmissionUrl());
            homeworkProgress.setDueDate(gradeEntity.getDueAt());
        });
    }

    public UserDto getUserData()
    {
        return userData;
    }

    public void setUserData(UserDto userData)
    {
        this.userData = userData;
    }

    public List<EnrolledSessionProgressModel> getEnrolledSessions()
    {
        return enrolledSessions;
    }

    public void setEnrolledSessions(List<EnrolledSessionProgressModel> enrolledSessions)
    {
        this.enrolledSessions = enrolledSessions;
    }
}