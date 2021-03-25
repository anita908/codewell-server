package com.codewell.server.config;

import com.codewell.server.config.settings.JerseySettings;
import com.codewell.server.config.settings.SwaggerFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.Collections;
import java.util.EnumSet;

import static com.codewell.server.config.settings.JerseySettings.CONTEXT_PATH;

@Configuration
public class WebConfig
{
    @Bean
    public ServletRegistrationBean<ServletContainer> jerseyServletRegistration(final JerseySettings resourceConfig)
    {
        final String path = CONTEXT_PATH + "/*";
        final ServletRegistrationBean<ServletContainer> registration = new ServletRegistrationBean<>(new ServletContainer(resourceConfig), path);
        registration.setName(resourceConfig.getApplicationName());
        registration.setLoadOnStartup(0);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<SwaggerFilter> swaggerFilterRegistration()
    {
        FilterRegistrationBean<SwaggerFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SwaggerFilter());
        registration.setUrlPatterns(Collections.singletonList("/"));
        registration.setOrder(0);
        registration.setName("swaggerFilter");
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }
}
