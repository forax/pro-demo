package com.github.forax.pro.example.springboot;

import java.util.Set;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
	  // spring boot component scan fails if modules are linked with jlink
	  // i.e. are not loaded by the application classloader 
		//SpringApplication.run(DemoApplication.class, args);
	  
	  // explicitly register the REST controller
	  SpringApplication app = new SpringApplication(DemoApplication.class);
	  app.setSources(Set.of(ModuleController.class.getName()));
	  app.run(args);
	}
}
