package com.hamadoutech.demominio;

import com.hamadoutech.demominio.config.MinioPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MinioPropertiesConfig.class)
public class DemoMinioApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMinioApplication.class, args);
	}

}
