package com.ypt.springboot.utils;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioProp {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}

