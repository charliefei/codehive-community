package com.feirui.oss.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 本地文件存储配置类
 */
@Data
@Configuration
public class LocalConfig {

    @Value("${local.url:localhost:4000}")
    private String url;

    /**
     * 基础路径
     */
    @Value("${local.basePath:/codehive/fileUpload}")
    private String basePath;

    /**
     * 加密开关
     */
    @Value("${local.passwordSwitch:false}")
    private Boolean passwordSwitch;

}
