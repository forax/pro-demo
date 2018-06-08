package com.github.forax.pro.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({ ModuleController.class })   // spring boot component scan fails if modules are linked with jlink
public class DemoApplication {
	public static void main(String[] args) {
	  SpringApplication.run(DemoApplication.class, args);
	}
}
