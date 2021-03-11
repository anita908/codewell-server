package com.codewell.server.dto;

public class HomeworkVideoDto
{
    private Integer id;
    private Integer homeworkId;
    private String name;
    private String storageUrl;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
