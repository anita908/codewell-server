package com.codewell.server.persistence.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "homework")
public class HomeworkEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "chapter_no")
    private Integer courseNo;

    @Column(name = "name")
    private String name;

    @Column(name = "storage_url")
    private String storageUrl;

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

    public Integer getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Integer courseId)
    {
        this.courseId = courseId;
    }

    public Integer getCourseNo()
    {
        return courseNo;
    }

    public void setCourseNo(Integer courseNo)
    {
        this.courseNo = courseNo;
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
