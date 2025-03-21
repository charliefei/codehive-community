package com.feirui.circle.server;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.feirui.circle.server.ai.ChatRequest;
import com.feirui.circle.server.ai.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

@Slf4j
public class AiChatTest {

    @Test
    public void testAIChat() {
        String query = "帮我用java解决力扣上的接雨水问题";
        try {
            // 构建 deepseek 请求对象
            ChatRequest request = ChatRequest.builder()
                    .model("deepseek-chat")
                    .temperature(0.7)
                    .max_tokens(1000)
                    .messages(Collections.singletonList(new ChatRequest.Message("user", query)))
                    .build();

            // 执行 HTTP 请求
            HttpResponse execute = HttpUtil.createRequest(Method.POST, "https://api.deepseek.com/chat/completions")
                    .body(JSONUtil.toJsonStr(request))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer sk-1e0b0e720dd04896aceb7c6e9d21b3f1")
                    .header("Accept", "application/json")
                    .execute();

            // 获取并日志记录响应内容
            String resp = execute.body();

            // 解析响应并提取内容
            ChatResponse chatResponse = JSONUtil.toBean(resp, ChatResponse.class);
            System.out.println(JSONUtil.toJsonStr(chatResponse));
            String str = Optional.ofNullable(chatResponse)
                    .map(ChatResponse::getChoices)
                    .filter(choices -> !choices.isEmpty())
                    .map(choices -> choices.get(0))
                    .map(choice -> choice.getMessage().getContent())
                    .orElseThrow(() -> new RuntimeException("Empty response"));
            System.out.println("------------------------");
            System.out.println(str);
        } catch (Exception e) {
            log.error("Error generating response for query: {}", query, e);
            throw new RuntimeException("Failed to generate response", e);
        }
    }

}
