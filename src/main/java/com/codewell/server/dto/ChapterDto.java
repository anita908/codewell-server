package com.codewell.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChapterDto
{
    private Integer id;
    private Integer courseId;
    private Integer chapterNo;
    private String name;
    private String slidesLink;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Integer courseId)
    {
        this.courseId = courseId;
    }

    public Integer getChapterNo()
    {
        return chapterNo;
    }

    public void setChapterNo(Integer chapterNo)
    {
        this.chapterNo = chapterNo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSlidesLink()
    {
        return slidesLink;
    }

    public void setSlidesLink(String slidesLink)
    {
        this.slidesLink = slidesLink;
    }
}
