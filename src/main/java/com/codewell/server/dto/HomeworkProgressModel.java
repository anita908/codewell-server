package com.codewell.server.dto;

public class HomeworkProgressModel
{
    private Integer homeworkId;
    private String homeworkName;
    private String homeworkLink;
    private Double homeworkScore;
    private String submitted = "false";

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

    public Double getHomeworkScore()
    {
        return homeworkScore;
    }

    public void setHomeworkScore(Double homeworkScore)
    {
        this.homeworkScore = homeworkScore;
    }

    public String getSubmitted()
    {
        return submitted;
    }

    public void setSubmitted(String submitted)
    {
        this.submitted = submitted;
    }
}