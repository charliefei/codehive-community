package com.feirui.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.ai.deepseek")
public class DeepSeekConfig {
    private String apiKey;
    private String apiUrl;
    private String model;
    private Double temperature;
    private Integer maxTokens;

    {
        apiKey = "";
        apiUrl = "https://api.deepseek.com/chat/completions";
        model = "deepseek-chat";
        temperature = 0.5;
        maxTokens = 1000;
    }
}