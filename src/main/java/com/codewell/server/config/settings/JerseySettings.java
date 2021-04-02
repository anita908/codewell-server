package com.codewell.server.config.settings;

import com.codewell.server.config.filter.CorsFilter;
import com.codewell.server.config.filter.HttpAdminAuthenticationFilter;
import com.codewell.server.config.filter.HttpAuthenticationFilter;
import com.codewell.server.exception.GeneralExceptionMapper;
import com.codewell.server.exception.IllegalArgumentExceptionMapper;
import com.codewell.server.web.*;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_DESCRIPTION;
import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_TITLE;

@Named
@Singleton
public class JerseySettings extends ResourceConfig
{
    public static final String APP_NAME = "Codewell Backend";
    public static final String CONTEXT_PATH = "/api";

    public JerseySettings()
    {
        this.registerControllers();
        this.registerFilters();
        this.registerMappers();
        this.setApplicationName(APP_NAME);
        this.initializeSwagger();
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
        this.register(GradesController.class);
        this.register(HealthController.class);
    }

    private void registerFilters()
    {
        this.register(CorsFilter.class);
        this.register(HttpAuthenticationFilter.class);
        this.register(HttpAdminAuthenticationFilter.class);
    }

    private void registerMappers()
    {
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
        server.setUrl(CONTEXT_PATH);

        openApi.setInfo(info);
        openApi.setServers(List.of(server));

        SwaggerConfiguration openApiConfiguration = new SwaggerConfiguration();
        openApiConfiguration.setOpenAPI(openApi);
        OpenApiResource resource = new OpenApiResource();
        resource.setOpenApiConfiguration(openApiConfiguration);

        this.register(resource);
    }
}
