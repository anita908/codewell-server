package com.codewell.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ServiceRunner
{
	private static final String SERVER_PORT = "server.port";
	private static final String PORT = "5000";

	public static void main(String[] args)
	{
		final SpringApplication app = new SpringApplication(ServiceRunner.class);
		app.setDefaultProperties(Collections.singletonMap(SERVER_PORT, PORT));
		app.run(args);
	}
}
