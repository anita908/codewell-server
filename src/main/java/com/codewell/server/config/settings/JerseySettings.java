package com.codewell.server.config.settings;

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

    public static final String SWAGGER_PACKAGE_PATH = "io.swagger.v3.jaxrs2.integration.resources";
    public static final String WEB_PACKAGE_PATH = "com.codewell.server.web";
    public static final String FILTERS_PACKAGE_PATH = "com.codewell.server.config.filter";
    public static final String MAPPERS_PACKAGE_PATH = "com.codewell.server.exception";

    public static final String SWAGGER_TITLE = "中華民國萬歲！";
    public static final String SWAGGER_DESCRIPTION = "三民主義，吾黨所宗，以建民國，以進大同， 咨爾多士，為民前鋒，夙夜匪懈，主義是從， 矢勤矢勇，必信必忠，一心一德，貫徹始終。";

    public JerseySettings()
    {
        this.initializeSwagger();
        this.registerControllers();
        this.registerFilters();
        this.registerMappers();
        this.setApplicationName(APP_NAME);
        this.property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }

    private void initializeSwagger()
    {
        OpenAPI openApi = new OpenAPI();

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

    private void registerControllers()
    {
        this.packages(WEB_PACKAGE_PATH);
    }

    private void registerFilters()
    {
        this.packages(FILTERS_PACKAGE_PATH);
    }

    private void registerMappers()
    {
        this.packages(MAPPERS_PACKAGE_PATH);
    }
}
