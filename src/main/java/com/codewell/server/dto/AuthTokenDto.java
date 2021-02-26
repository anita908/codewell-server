package com.codewell.server.dto;

import java.time.OffsetDateTime;

public class AuthTokenDto
{
    private String result;
    private String jwt;
    private OffsetDateTime tokenAssignDate;

    public AuthTokenDto() {}

    public AuthTokenDto(final String result, final String jwt, final OffsetDateTime tokenAssignDate)
    {
        this.result = result;
        this.jwt = jwt;
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

    public OffsetDateTime getTokenAssignDate()
    {
        return tokenAssignDate;
    }

    public void setTokenAssignDate(OffsetDateTime tokenAssignDate)
    {
        this.tokenAssignDate = tokenAssignDate;
    }
}
