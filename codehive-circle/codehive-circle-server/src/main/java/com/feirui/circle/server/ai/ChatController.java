package com.feirui.circle.server.ai;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/deepseek")
@CrossOrigin(origins = "*")
public class ChatController {

    @Resource
    private DeepSeekService deepSeekService;

    @GetMapping("/chat")
    public String chat(String query) {
        return deepSeekService.generateResponse(query);
    }

}
