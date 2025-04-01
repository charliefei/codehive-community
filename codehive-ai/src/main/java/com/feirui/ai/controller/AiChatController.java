package com.feirui.ai.controller;

import cn.hutool.json.JSONUtil;
import com.feirui.ai.config.DeepSeekConfig;
import com.feirui.ai.domain.ChatMsgReq;
import com.feirui.ai.domain.ChatRequest;
import com.feirui.ai.service.DeepSeekService;
import com.feirui.ai.utils.SimpleTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.*;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping("/ai")
@Slf4j
@CrossOrigin(origins = "*")
public class AiChatController {

    @Resource
    private DeepSeekService deepSeekService;
    @Resource
    private DeepSeekConfig deepseekConfig;

    @GetMapping(value = "/chat/stream", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(String query) {
        log.info("query: {}", query);
        ChatRequest question = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Collections.singletonList(new ChatRequest.Message("user", query)))
                .stream(true)
                .build();
        return deepSeekService.generateResponseAsStream(question);
    }

    @GetMapping(value = "/chat/stream/v2", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatV2(String query) {
        log.info("query: {}", query);
        ChatRequest question = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Collections.singletonList(new ChatRequest.Message("user", query)))
                .stream(true)
                .build();
        return deepSeekService.generateResponseAsStreamV2(question);
    }

    @PostMapping(value = "/chat/stream/v3", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatV3(@RequestBody Map<String, Object> map) {
        log.info("map: {}", JSONUtil.toJsonStr(map));
        ChatRequest question = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Collections.singletonList(new ChatRequest.Message("user", String.valueOf(map.get("query")))))
                .stream(true)
                .build();
        return deepSeekService.generateResponseAsStreamV2(question);
    }

    @PostMapping(value = "/chat/stream/v4", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatV4(@RequestBody ChatMsgReq req) {
        log.info("req: {}", JSONUtil.toJsonStr(req));
        List<ChatRequest.Message> messages = new ArrayList<>(
                JSONUtil.toList(req.getHistory(), ChatRequest.Message.class));

        // 创建用户消息并计算token
        ChatRequest.Message userMessage = new ChatRequest.Message("user", req.getQuery());
        int userTokenCount = SimpleTokenUtils.countTokens(userMessage.getContent());

        int allowedHistoryToken = deepseekConfig.getMaxTokens() - userTokenCount;

        // 计算并截断历史记录
        int currentHistoryToken = messages.stream()
                .mapToInt(msg -> SimpleTokenUtils.countTokens(msg.getContent()))
                .sum();

        // 移除最旧的消息直到满足token限制
        while (currentHistoryToken > allowedHistoryToken && !messages.isEmpty()) {
            ChatRequest.Message removed = messages.remove(0);
            currentHistoryToken -= SimpleTokenUtils.countTokens(removed.getContent());
        }

        messages.add(userMessage);
        ChatRequest question = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(messages)
                .stream(true)
                .build();
        return deepSeekService.generateResponseAsStreamV2(question);
    }

    @GetMapping(value = "/chat")
    public String chatAsText(String query) {
        log.info("query: {}", query);
        ChatRequest question = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Collections.singletonList(new ChatRequest.Message("user", query)))
                .build();
        return deepSeekService.generateResponse(question);
    }

}
