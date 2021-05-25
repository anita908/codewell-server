package com.codewell.server.dto;

public class RecordingDto
{
    private Integer id;
    private Integer sessionId;
    private Integer chapterNo;
    private String url;

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

    public Integer getChapterNo()
    {
        return chapterNo;
    }

    public void setChapterNo(Integer chapterNo)
    {
        this.chapterNo = chapterNo;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
