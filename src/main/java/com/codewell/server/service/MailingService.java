package com.codewell.server.service;

public interface MailingService
{
    void sendEmail(final String to, final String subject, final String message) throws Exception;
}
