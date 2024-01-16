package com.feirui.oss.config;

import com.feirui.oss.adapter.MinioOssAdapter;
import com.feirui.oss.adapter.OssAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {
    @Value("${oss.type}")
    private String ossType;

    @Bean
    public OssAdapter ossAdapter() {
        if ("minio".equals(ossType)) {
            return new MinioOssAdapter();
        } else {
            throw new IllegalArgumentException("未找到对应的文件存储处理器");
        }
    }
}
