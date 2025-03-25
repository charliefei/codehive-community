package com.feirui.ai.controller;

import com.feirui.ai.domain.ChatRequest;
import com.feirui.ai.service.DeepSeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Collections;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequestMapping("/ai")
@Slf4j
@CrossOrigin(origins = "*")
public class AiChatController {

    @Resource
    private DeepSeekService deepSeekService;

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
