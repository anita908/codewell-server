package com.codewell.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthTokenDto
{
    private String result;
    private String jwt;
    private boolean isAdmin;
    private OffsetDateTime tokenAssignDate;

    public AuthTokenDto() {}

    public AuthTokenDto(final String result, final String jwt, final boolean isAdmin, final OffsetDateTime tokenAssignDate)
    {
        this.result = result;
        this.jwt = jwt;
        this.isAdmin = isAdmin;
        this.tokenAssignDate = tokenAssignDate;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getJwt()
    {
        return jwt;
    }

    public void setJwt(String jwt)
    {
        this.jwt = jwt;
    }

    public boolean getIsAdmin()
    {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    public OffsetDateTime getTokenAssignDate()
    {
        return tokenAssignDate;
    }

    public void setTokenAssignDate(OffsetDateTime tokenAssignDate)
    {
        this.tokenAssignDate = tokenAssignDate;
    }
}
