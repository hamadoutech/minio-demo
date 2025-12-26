package com.hamadoutech.demominio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioPropertiesConfig (String endpoint, String accessKey, String secretKey, String bucketName) {}
