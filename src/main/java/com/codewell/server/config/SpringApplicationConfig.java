package com.codewell.server.config;

import com.codewell.server.web.DefaultController;
import com.codewell.server.web.UserController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@Configuration
@Import({DatabaseConnectionConfig.class, HttpConfig.class})
@ComponentScan
public class SpringApplicationConfig extends ResourceConfig
{
    @Autowired
    private Environment env;

    public SpringApplicationConfig()
    {
        register(DefaultController.class);
        register(UserController.class);
    }
}
