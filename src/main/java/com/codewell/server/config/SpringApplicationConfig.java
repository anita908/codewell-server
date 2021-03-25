package com.codewell.server.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.codewell.server.config.settings.JerseySettings;
import com.codewell.server.config.settings.SwaggerFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.DispatcherType;
import java.util.*;

@Configuration
@Import({DatabaseConnectionConfig.class})
public class SpringApplicationConfig
{
    @Autowired
    private Environment env;

    @Bean
    public FilterRegistrationBean<SwaggerFilter> swaggerFilterRegistration()
    {
        FilterRegistrationBean<SwaggerFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SwaggerFilter(JerseySettings.CONTEXT_PATH));
        registration.setUrlPatterns(Collections.singletonList("/"));
        registration.setOrder(0);
        registration.setName("swaggerFilter");
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }

    @Bean
    public ServletRegistrationBean<ServletContainer> jerseyServletRegistration(final JerseySettings resourceConfig)
    {
        final String path = JerseySettings.CONTEXT_PATH + "/*";
        final ServletRegistrationBean<ServletContainer> registration = new ServletRegistrationBean<>(new ServletContainer(resourceConfig), path);
        registration.setName(resourceConfig.getApplicationName());
        registration.setLoadOnStartup(0);
        return registration;
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
