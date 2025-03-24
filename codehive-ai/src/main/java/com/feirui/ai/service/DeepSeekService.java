package com.feirui.ai.service;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.feirui.ai.config.DeepSeekConfig;
import com.feirui.ai.domain.ChatRequest;
import com.feirui.ai.domain.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
public class DeepSeekService {

    @Resource
    private DeepSeekConfig deepSeekConfig;

    public String generateResponse(ChatRequest request) {
        try {
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
            log.error("Error generating response for request: {}", JSONUtil.toJsonStr(request), e);
            throw new RuntimeException("Failed to generate response", e);
        }
    }

    public Flux<String> generateResponseAsStream(ChatRequest request) {
        return WebClient.builder()
                .baseUrl(deepSeekConfig.getApiUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + deepSeekConfig.getApiKey())
                .build()
                .post()
                .body(BodyInserters.fromObject(request))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(this::extractStreamResponse);
    }

    /**
     * 提取回复文本内容
     */
    private String extractResponse(ChatResponse response) {
        return Optional.ofNullable(response)
                .map(ChatResponse::getChoices)
                .filter(choices -> !choices.isEmpty())
                .map(choices -> choices.get(0))
                .map(choice -> choice.getMessage().getContent())
                .orElseThrow(() -> new RuntimeException("Empty response"));
    }

    /**
     * 流式提取回复文本内容
     */
    private Flux<String> extractStreamResponse(String result) {
        if ("[DONE]".equals(result)) {
            return Flux.empty();
        } else {
            try {
                ChatResponse response = JSONUtil.toBean(result, ChatResponse.class);
                String content = response.getChoices().get(0).getDelta().getContent();
                return Flux.just(content);
            } catch (Exception e) {
                log.error("解析失败: {}", result, e);
            }
        }
        return Flux.empty();
    }

}
