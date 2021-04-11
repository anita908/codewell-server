package com.codewell.server.dto;

import java.time.OffsetDateTime;

public class EnrollmentDto
{
    private Integer id;
    private Integer sessionId;
    private String userId;
    private OffsetDateTime enrollDate;
    private Integer currentChapter;
    private String graduated;
    private Double overallGrade;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(Integer sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public OffsetDateTime getEnrollDate()
    {
        return enrollDate;
    }

    public void setEnrollDate(OffsetDateTime enrollDate)
    {
        this.enrollDate = enrollDate;
    }

    public Integer getCurrentChapter()
    {
        return currentChapter;
    }

    public void setCurrentChapter(Integer currentChapter)
    {
        this.currentChapter = currentChapter;
    }

    public String getGraduated()
    {
        return graduated;
    }

    public void setGraduated(String graduated)
    {
        this.graduated = graduated;
    }

    public Double getOverallGrade()
    {
        return overallGrade;
    }

    public void setOverallGrade(Double overallGrade)
    {
        this.overallGrade = overallGrade;
    }
}
