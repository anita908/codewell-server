package com.codewell.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceRunner
{
	public static void main(String[] args)
	{
		System.setProperty("server.servlet.context-path", "/api");
		SpringApplication.run(ServiceRunner.class, args);
	}
}
