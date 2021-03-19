package com.codewell.server.dto;

public class HomeworkProgressModel
{
    private Integer homeworkId;
    private String homeworkName;
    private String homeworkLink;
    private Float homeworkScore;

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

    public String getHomeworkLink()
    {
        return homeworkLink;
    }

    public void setHomeworkLink(String homeworkLink)
    {
        this.homeworkLink = homeworkLink;
    }

    public Float getHomeworkScore()
    {
        return homeworkScore;
    }

    public void setHomeworkScore(Float homeworkScore)
    {
        this.homeworkScore = homeworkScore;
    }
}