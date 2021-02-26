package com.codewell.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ServiceRunner
{
	public static void main(String[] args)
	{
		System.setProperty("server.servlet.context-path", "/api");
		final SpringApplication app = new SpringApplication(ServiceRunner.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "5000"));
		app.run(args);
	}
}
