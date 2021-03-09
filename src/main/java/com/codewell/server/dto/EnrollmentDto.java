package com.codewell.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentDto
{
    private Integer id;
    private Integer sessionId;
    private Integer courseId;
    private String courseName;
    private OffsetDateTime enrollDate;
    private OffsetDateTime beginDate;
    private OffsetDateTime endDate;
    private String graduated;
    private Float overallGrade;
    private List<GradeDto> grades = new ArrayList<>();

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

    public Integer getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Integer courseId)
    {
        this.courseId = courseId;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public OffsetDateTime getEnrollDate()
    {
        return enrollDate;
    }

    public void setEnrollDate(OffsetDateTime enrollDate)
    {
        this.enrollDate = enrollDate;
    }

    public OffsetDateTime getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(OffsetDateTime beginDate)
    {
        this.beginDate = beginDate;
    }

    public OffsetDateTime getEndDate()
    {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate)
    {
        this.endDate = endDate;
    }

    public String getGraduated()
    {
        return graduated;
    }

    public void setGraduated(String graduated)
    {
        this.graduated = graduated;
    }

    public Float getOverallGrade()
    {
        return overallGrade;
    }

    public void setOverallGrade(Float overallGrade)
    {
        this.overallGrade = overallGrade;
    }

    public List<GradeDto> getGrades()
    {
        return grades;
    }

    public void setGrades(List<GradeDto> grades)
    {
        this.grades = grades;
    }
}
