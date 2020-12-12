package com.nickmafra.demo;

import com.github.kklisura.java.processing.annotations.PropertySourceConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@PropertySourceConstants(resourceName = "messages.properties", className = "Messages_")
@PropertySourceConstants(resourceName = "application.properties", className = "ApplicationProperties_")
public class ProjetoModeloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoModeloApplication.class, args);
	}

}
