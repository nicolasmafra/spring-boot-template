package com.nickmafra.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ProjetoModeloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoModeloApplication.class, args);
	}

}
