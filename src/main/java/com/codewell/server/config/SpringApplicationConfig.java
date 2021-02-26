package com.codewell.server.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

@Configuration
@Import({DatabaseConnectionConfig.class, HttpCorsConfig.class})
@Component
@Singleton
public class SpringApplicationConfig
{
    @Autowired
    private Environment env;

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
