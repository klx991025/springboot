package com.ypt.springboot.config;

import com.ypt.springboot.utils.MinioProp;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Configuration
public class MinioConfiguration {
    @Autowired
    private MinioProp minioProp;

    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        MinioClient client = new MinioClient(minioProp.getEndpoint(), minioProp.getAccessKey(), minioProp.getSecretKey());
        return client;
    }



}
