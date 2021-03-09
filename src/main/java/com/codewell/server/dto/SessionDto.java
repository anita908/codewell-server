package com.codewell.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionDto
{
    private Integer id;
    private CourseDto course;
    private OffsetDateTime beginDate;
    private OffsetDateTime endDate;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public CourseDto getCourse()
    {
        return course;
    }

    public void setCourse(CourseDto course)
    {
        this.course = course;
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
}
