package com.codewell.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentDto
{
    private Integer id;
    private SessionDto session;
    private String userId;
    private OffsetDateTime enrollDate;
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

    public SessionDto getSession()
    {
        return session;
    }

    public void setSession(SessionDto session)
    {
        this.session = session;
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
