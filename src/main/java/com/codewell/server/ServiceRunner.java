package com.codewell.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Collections;
import java.util.Map;

//@SpringBootApplication
//public class ServiceRunner
//{
//	private static final String SERVER_PORT = "server.port";
//	private static final String PORT = "5000";
//
//	public static void main(String[] args)
//	{
//		final SpringApplication app = new SpringApplication(ServiceRunner.class);
//		app.setDefaultProperties(Collections.singletonMap(SERVER_PORT, PORT));
//		app.run(args);
//	}
//}

@SpringBootApplication
@ServletComponentScan(basePackages = "com.codewell.server")
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
