package com.codewell.server.config.settings;

import com.codewell.server.config.filter.CorsFilter;
import com.codewell.server.config.filter.HttpAdminAuthenticationFilter;
import com.codewell.server.config.filter.HttpAuthenticationFilter;
import com.codewell.server.exception.ExceptionResponse;
import com.codewell.server.exception.GeneralExceptionMapper;
import com.codewell.server.exception.IllegalArgumentExceptionMapper;
import com.codewell.server.web.*;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import java.util.List;

@Component
@Singleton
@ApplicationPath(JerseySettings.CONTEXT_PATH)
public class JerseySettings extends ResourceConfig
{
    public static final String APP_NAME = "Codewell Backend";
    public static final String CONTEXT_PATH = "/api";

    public static final String WEB_PACKAGE_PATH = "com.codewell.server.web";
    public static final String FILTERS_PACKAGE_PATH = "com.codewell.server.config.filter";
    public static final String MAPPERS_PACKAGE_PATH = "com.codewell.server.exception";

    public static final String SWAGGER_TITLE = "中華民國萬歲！";
    public static final String SWAGGER_DESCRIPTION = "三民主義，吾黨所宗，以建民國，以進大同， 咨爾多士，為民前鋒，夙夜匪懈，主義是從， 矢勤矢勇，必信必忠，一心一德，貫徹始終。";

    public JerseySettings()
    {
        this.registerControllers();
        this.registerFilters();
        this.registerMappers();
        this.initializeSwagger();
        this.setApplicationName(APP_NAME);
        this.property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }

    private void registerControllers()
    {
        this.register(AdminController.class);
        this.register(AuthController.class);
        this.register(ChapterController.class);
        this.register(EnrollmentController.class);
        this.register(HomeworkController.class);
        this.register(LearningController.class);
        this.register(UserController.class);
    }

    private void registerFilters()
    {
        this.register(CorsFilter.class);
        this.register(HttpAuthenticationFilter.class);
        this.register(HttpAdminAuthenticationFilter.class);
    }

    private void registerMappers()
    {
        this.register(ExceptionResponse.class);
        this.register(GeneralExceptionMapper.class);
        this.register(IllegalArgumentExceptionMapper.class);
    }

    private void initializeSwagger()
    {
        final OpenAPI openApi = new OpenAPI();

        final Info info = new Info();
        info.setTitle(SWAGGER_TITLE);
        info.setDescription(SWAGGER_DESCRIPTION);

        final Server server = new Server();
        server.setUrl("/");

        openApi.setInfo(info);
        openApi.setServers(List.of(server));

        SwaggerConfiguration openApiConfiguration = new SwaggerConfiguration();
        openApiConfiguration.setOpenAPI(openApi);
        OpenApiResource resource = new OpenApiResource();
        resource.setOpenApiConfiguration(openApiConfiguration);
        this.register(resource);
    }
}
