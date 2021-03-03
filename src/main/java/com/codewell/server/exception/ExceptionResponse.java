package com.codewell.server.exception;

import java.util.Arrays;

public class ExceptionResponse
{
    private String message;
    private String errorType;
    private String stackTrace;

    public ExceptionResponse(final Exception exception)
    {
        message = exception.getMessage();
        errorType = exception.getClass().toString();
        stackTrace = Arrays.toString(exception.getStackTrace());
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getErrorType()
    {
        return errorType;
    }

    public void setErrorType(String errorType)
    {
        this.errorType = errorType;
    }

    public String getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }
}
