package com.codewell.server.persistence.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "recordings")
public class RecordingEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "chapter_no")
    private Integer chapterNo;

    @Column(name = "url")
    private String url;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

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

    public OffsetDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
