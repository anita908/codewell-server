package com.codewell.server.config.settings;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SwaggerFilter implements Filter
{
    private String contextPath;

    private static final String WEB_JAR_PATH = "webjars/swagger-ui/3.24.0/index.html";

    public SwaggerFilter(final String contextPath)
    {
        this.contextPath = contextPath;
    }

    @Override
    public void init(final FilterConfig filterConfig) {}

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws ServletException, IOException
    {
        final HttpServletRequest request = (HttpServletRequest) req;
        if (request.getRequestURI().equals("/"))
        {
            final String requestUri = request.getRequestURI() + WEB_JAR_PATH + "?url=" + contextPath + "/openapi.json";
            final HttpServletResponse response = (HttpServletResponse) res;
            response.sendRedirect(requestUri);
        }
        else
        {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {}
}