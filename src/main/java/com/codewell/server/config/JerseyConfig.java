package com.codewell.server.config;

import com.codewell.server.exception.GeneralExceptionMapper;
import com.codewell.server.exception.IllegalArgumentExceptionMapper;
import com.codewell.server.web.AuthController;
import com.codewell.server.web.DefaultController;
import com.codewell.server.web.UserController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

@Component
@Singleton
public class JerseyConfig extends ResourceConfig
{
    public JerseyConfig()
    {
        register(GeneralExceptionMapper.class);
        register(IllegalArgumentExceptionMapper.class);
        register(HttpAuthenticationConfig.class);
        register(DefaultController.class);
        register(UserController.class);
        register(AuthController.class);
    }
}
