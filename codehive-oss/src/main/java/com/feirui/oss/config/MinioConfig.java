package com.feirui.oss.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    /**
     * minio url
     */
    @Value("${minio.url}")
    private String url;
    /**
     * minio 账户
     */
    @Value("${minio.accessKey}")
    private String accessKey;
    /**
     * minio 密码
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
