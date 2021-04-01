package com.codewell.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Named
@Singleton
public class MailingServiceImpl implements MailingService
{
    @Autowired
    private Environment env;

    private static final String GMAIL_SMTP_HOST = "smtp.gmail.com";
    private static final String SOCKET_PORT = "465";

    @Override
    public void sendEmail(final String to, final String subject, final String message) throws Exception
    {
        Assert.hasText(to, "Recipient must be provided");
        Assert.hasText(subject, "Subject must be provided");
        Assert.hasText(message, "Message body must be provided");

        final Session session = createSession();
        try
        {
            final MimeMessage email = new MimeMessage(session);
            email.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            email.setSubject(subject);
            email.setContent(message, "text/html");
            Transport.send(email);
        }
        catch (MessagingException exception)
        {
            throw new Exception(exception.getMessage());
        }
    }

    private Session createSession()
    {
        final Properties props = new Properties();
        props.put("mail.smtp.host", GMAIL_SMTP_HOST);
        props.put("mail.smtp.port", SOCKET_PORT);
        props.put("mail.smtp.socketFactory.port", SOCKET_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        return Session.getDefaultInstance(props, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(env.getProperty("codewell.email.address"), env.getProperty("codewell.email.password"));
            }
        });
    }
}
