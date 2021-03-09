package com.codewell.server.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_tokens")
public class UserTokenEntity
{
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "token")
    private String token;

    @Column(name = "salt")
    private String salt;

    @Column(name = "expire_date")
    private OffsetDateTime expireDate;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public OffsetDateTime getExpireDate()
    {
        return expireDate;
    }

    public void setExpireDate(OffsetDateTime expireDate)
    {
        this.expireDate = expireDate;
    }

    public OffsetDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
