package com.codewell.server.dto;

import java.util.ArrayList;
import java.util.List;

public class ChapterProgressModel
{
    private Integer chapterId;
    private Integer chapterNo;
    private String chapterName;
    private Integer homeworkId;
    private String homeworkName;
    private Float homeworkScore;
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
        private Integer homeworkId;
        private String homeworkName;
        private Float homeworkScore;
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

        public Builder homeworkId(Integer homeworkId)
        {
            this.homeworkId = homeworkId;
            return this;
        }

        public Builder homeworkName(String homeworkName)
        {
            this.homeworkName = homeworkName;
            return this;
        }

        public Builder homeworkScore(Float homeworkScore)
        {
            this.homeworkScore = homeworkScore;
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
            model.setHomeworkId(this.homeworkId);
            model.setHomeworkName(this.homeworkName);
            model.setHomeworkScore(this.homeworkScore);
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

    public Integer getHomeworkId()
    {
        return homeworkId;
    }

    public void setHomeworkId(Integer homeworkId)
    {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkName()
    {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName)
    {
        this.homeworkName = homeworkName;
    }

    public Float getHomeworkScore()
    {
        return homeworkScore;
    }

    public void setHomeworkScore(Float homeworkScore)
    {
        this.homeworkScore = homeworkScore;
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
