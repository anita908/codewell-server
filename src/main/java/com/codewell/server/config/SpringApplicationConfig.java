package com.codewell.server.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Import({DatabaseConnectionConfig.class, WebConfig.class})
public class SpringApplicationConfig
{
    @Autowired
    private Environment env;

    @Bean
    public AwsBasicCredentials awsCredentialProvider()
    {
        return AwsBasicCredentials.create(env.getProperty("aws.access.key"), env.getProperty("aws.secret.key"));
    }

    @Bean
    public S3Client s3Client()
    {
        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(this.awsCredentialProvider()))
            .region(Region.US_WEST_2)
            .build();
    }

    @Bean
    public PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Algorithm algorithm()
    {
        return Algorithm.HMAC256("secret");
    }
}
