package com.codewell.server.persistence.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "enrollment")
public class EnrollmentEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id")
    private SessionEntity session;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "enroll_date")
    private OffsetDateTime enrollDate;

    @Column(name = "current_chapter")
    private Integer currentChapter;

    @Column(name = "graduated")
    private String graduated;

    @Column(name = "overall_grade")
    private Double overallGrade;

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

    public SessionEntity getSession()
    {
        return session;
    }

    public void setSession(SessionEntity session)
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

    public Double getOverallGrade()
    {
        return overallGrade;
    }

    public void setOverallGrade(Double overallGrade)
    {
        this.overallGrade = overallGrade;
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
