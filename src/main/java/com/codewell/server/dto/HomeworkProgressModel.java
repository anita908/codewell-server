package com.codewell.server.dto;

import java.time.OffsetDateTime;

public class HomeworkProgressModel
{
    private Integer homeworkId;
    private String homeworkName;
    private String homeworkLink;
    private Double homeworkScore;
    private String submitted = "false";
    private String submissionUrl;
    private String submissionFeedback;
    private OffsetDateTime dueDate;
    private OffsetDateTime submissionDate;

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

    public String getSubmissionUrl()
    {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl)
    {
        this.submissionUrl = submissionUrl;
    }

    public String getSubmissionFeedback()
    {
        return submissionFeedback;
    }

    public void setSubmissionFeedback(String submissionFeedback)
    {
        this.submissionFeedback = submissionFeedback;
    }

    public OffsetDateTime getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate)
    {
        this.dueDate = dueDate;
    }

    public OffsetDateTime getSubmissionDate()
    {
        return submissionDate;
    }

    public void setSubmissionDate(OffsetDateTime submissionDate)
    {
        this.submissionDate = submissionDate;
    }
}