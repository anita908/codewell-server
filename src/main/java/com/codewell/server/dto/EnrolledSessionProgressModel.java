package com.codewell.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrolledSessionProgressModel
{
    private Integer enrollmentId;
    private Integer sessionId;
    private Integer courseId;
    private String courseName;
    private OffsetDateTime enrollDate;
    private OffsetDateTime beginDate;
    private OffsetDateTime endDate;
    private Integer currentChapter;
    private String graduated;
    private String withdrawn;
    private Double overallGrade;
    private List<ChapterProgressModel> sessionProgressModel = new ArrayList<>();

    public Integer getEnrollmentId()
    {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId)
    {
        this.enrollmentId = enrollmentId;
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

    public String getWithdrawn()
    {
        return withdrawn;
    }

    public void setWithdrawn(String withdrawn)
    {
        this.withdrawn = withdrawn;
    }

    public Double getOverallGrade()
    {
        return overallGrade;
    }

    public void setOverallGrade(Double overallGrade)
    {
        this.overallGrade = overallGrade;
    }

    public List<ChapterProgressModel> getSessionProgressModel()
    {
        return sessionProgressModel;
    }

    public void setSessionProgressModel(List<ChapterProgressModel> sessionProgressModel)
    {
        this.sessionProgressModel = sessionProgressModel;
    }
}
