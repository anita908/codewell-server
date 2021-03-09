package com.codewell.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeworkDto
{
    private Integer id;
    private Integer courseId;
    private Integer chapterNo;
    private String homeworkName;
    private String storageUrl;

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

    public String getHomeworkName()
    {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName)
    {
        this.homeworkName = homeworkName;
    }

    public String getStorageUrl()
    {
        return storageUrl;
    }

    public void setStorageUrl(String storageUrl)
    {
        this.storageUrl = storageUrl;
    }
}
