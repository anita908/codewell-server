package com.codewell.server;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Map;

@SpringBootApplication
@EnableAutoConfiguration
public class ServiceRunner extends SpringBootServletInitializer
{
	private static final String SERVER_PORT = "server.port";
	private static final String PORT = "5000";

	public static void main(String[] args)
	{
		Map<String, Object> props = Map.of(SERVER_PORT, PORT);
		new ServiceRunner().configure(new SpringApplicationBuilder(ServiceRunner.class).properties(props)).run(args);
	}
}
