package com.codewell.server.persistence.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "grades")
public class GradeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "session_id")
    private Integer sessionId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "homework_id")
    private HomeworkEntity homework;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "submission_url")
    private String submissionUrl;

    @Column(name = "score")
    private Double score;

    @Column(name = "due_at")
    private OffsetDateTime dueAt;

    @Column(name = "submitted")
    private String submitted;

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

    public HomeworkEntity getHomework()
    {
        return homework;
    }

    public void setHomework(HomeworkEntity homework)
    {
        this.homework = homework;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getSubmissionUrl()
    {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl)
    {
        this.submissionUrl = submissionUrl;
    }

    public Double getScore()
    {
        return score;
    }

    public void setScore(Double score)
    {
        this.score = score;
    }

    public OffsetDateTime getDueAt()
    {
        return dueAt;
    }

    public void setDueAt(OffsetDateTime dueAt)
    {
        this.dueAt = dueAt;
    }

    public String getSubmitted()
    {
        return submitted;
    }

    public void setSubmitted(String submitted)
    {
        this.submitted = submitted;
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
