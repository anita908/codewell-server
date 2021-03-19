package com.codewell.server.dto;

import java.util.ArrayList;
import java.util.List;

public class ChapterProgressModel
{
    private Integer chapterId;
    private Integer chapterNo;
    private String chapterName;
    private String slidesLink;
    private List<HomeworkProgressModel> homeworkProgress = new ArrayList<>();
    private List<ActivityDto> activities = new ArrayList<>();

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private Integer chapterId;
        private Integer chapterNo;
        private String chapterName;
        private String slidesLink;
        private List<HomeworkProgressModel> homeworkProgress = new ArrayList<>();
        private List<ActivityDto> activities = new ArrayList<>();

        public Builder chapterId(Integer chapterId)
        {
            this.chapterId = chapterId;
            return this;
        }

        public Builder chapterNo(Integer chapterNo)
        {
            this.chapterNo = chapterNo;
            return this;
        }

        public Builder chapterName(String chapterName)
        {
            this.chapterName = chapterName;
            return this;
        }

        public Builder slidesLink(String slidesLink)
        {
            this.slidesLink = slidesLink;
            return this;
        }

        public Builder homeworkProgress(List<HomeworkProgressModel> homeworkProgress)
        {
            this.homeworkProgress = homeworkProgress;
            return this;
        }

        public Builder activities(List<ActivityDto> activities)
        {
            this.activities = activities;
            return this;
        }

        public ChapterProgressModel build()
        {
            final ChapterProgressModel model = new ChapterProgressModel();
            model.setChapterId(this.chapterId);
            model.setChapterNo(this.chapterNo);
            model.setChapterName(this.chapterName);
            model.setSlidesLink(this.slidesLink);
            model.setHomeworkProgress(this.homeworkProgress);
            model.setActivities(this.activities);
            return model;
        }
    }

    public Integer getChapterId()
    {
        return chapterId;
    }

    public void setChapterId(Integer chapterId)
    {
        this.chapterId = chapterId;
    }

    public Integer getChapterNo()
    {
        return chapterNo;
    }

    public void setChapterNo(Integer chapterNo)
    {
        this.chapterNo = chapterNo;
    }

    public String getChapterName()
    {
        return chapterName;
    }

    public void setChapterName(String chapterName)
    {
        this.chapterName = chapterName;
    }

    public String getSlidesLink()
    {
        return slidesLink;
    }

    public void setSlidesLink(String slidesLink)
    {
        this.slidesLink = slidesLink;
    }

    public List<HomeworkProgressModel> getHomeworkProgress()
    {
        return homeworkProgress;
    }

    public void setHomeworkProgress(List<HomeworkProgressModel> homeworkProgress)
    {
        this.homeworkProgress = homeworkProgress;
    }

    public List<ActivityDto> getActivities()
    {
        return activities;
    }

    public void setActivities(List<ActivityDto> activities)
    {
        this.activities = activities;
    }
}
