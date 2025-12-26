package com.hamadoutech.demominio.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    private final MinioPropertiesConfig minioPropertiesConfig;

    public MinioConfig(final MinioPropertiesConfig minioPropertiesConfig) {
        this.minioPropertiesConfig = minioPropertiesConfig;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioPropertiesConfig.endpoint())
                .credentials(minioPropertiesConfig.accessKey(), minioPropertiesConfig.secretKey())
                .build();
    }

}
