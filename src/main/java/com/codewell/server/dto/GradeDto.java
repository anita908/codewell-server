package com.codewell.server.dto;

import java.time.OffsetDateTime;

public class GradeDto
{
    private Integer id;
    private Integer homeworkId;
    private String homeworkName;
    private String submissionUrl;
    private Double score;
    private String feedback;
    private OffsetDateTime dueDate;
    private String submitted = "false";
    private OffsetDateTime submissionDate;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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

    public String getSubmissionUrl()
    {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl)
    {
        this.submissionUrl = submissionUrl;
    }

    public Double getScore()
    {
        return score;
    }

    public void setScore(Double score)
    {
        this.score = score;
    }

    public String getFeedback()
    {
        return feedback;
    }

    public void setFeedback(String feedback)
    {
        this.feedback = feedback;
    }

    public OffsetDateTime getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted)
    {
        this.submitted = submitted;
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
