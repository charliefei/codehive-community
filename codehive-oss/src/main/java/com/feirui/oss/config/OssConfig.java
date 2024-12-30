package com.feirui.oss.config;

import com.feirui.oss.adapter.AliyunOssAdapter;
import com.feirui.oss.adapter.LocalOssAdapter;
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
        } else if ("aliyun".equals(ossType)) {
            return new AliyunOssAdapter();
        } else if ("local".equals(ossType)) {
            return new LocalOssAdapter();
        } else {
            throw new IllegalArgumentException("未找到对应的文件存储处理器");
        }
    }
}
