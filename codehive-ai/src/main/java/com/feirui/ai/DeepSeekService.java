package com.feirui.ai;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class DeepSeekService {

    @Resource
    private DeepSeekConfig deepSeekConfig;

    public String generateResponse(String query) {
        try {
            // 构建 deepseek 请求对象
            ChatRequest request = buildRequest(query);

            // 执行 HTTP 请求
            HttpResponse execute = HttpUtil.createRequest(Method.POST, deepSeekConfig.getApiUrl())
                    .body(JSONUtil.toJsonStr(request))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + deepSeekConfig.getApiKey())
                    .header("Accept", "application/json")
                    .execute();

            // 获取并日志记录响应内容
            String resp = execute.body();
            log.info("deepseek response: {}", resp);

            // 解析响应并提取内容
            ChatResponse chatResponse = JSONUtil.toBean(resp, ChatResponse.class);
            return extractResponse(chatResponse);
        } catch (Exception e) {
            log.error("Error generating response for query: {}", query, e);
            throw new RuntimeException("Failed to generate response", e);
        }
    }

    /**
     * 构建 deepseek 请求对象
     */
    private ChatRequest buildRequest(String query) {
        return ChatRequest.builder()
                .model(deepSeekConfig.getModel())
                .temperature(deepSeekConfig.getTemperature())
                .max_tokens(deepSeekConfig.getMaxTokens())
                .messages(Collections.singletonList(new ChatRequest.Message("user", query)))
                .build();
    }

    /**
     * 提取回复内容
     */
    private String extractResponse(ChatResponse response) {
        return Optional.ofNullable(response)
                .map(ChatResponse::getChoices)
                .filter(choices -> !choices.isEmpty())
                .map(choices -> choices.get(0))
                .map(choice -> choice.getMessage().getContent())
                .orElseThrow(() -> new RuntimeException("Empty response"));
    }

}
