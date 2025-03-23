package com.feirui.interview.server;

import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.feirui.ai.domain.ChatRequest;
import com.feirui.ai.service.DeepSeekService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
public class ApplicationTest {

    private static final String preset_context1 = "你现在是一名高级软件开发工程师，我接下来给你输入一些关键字，你给我生成1道面试题和标签，只用给定的json模板回答我。" +
            "json模板：{\"subjectName\": \"题目名称\", \"labelName\": \"题目标签\", \"subjectAnswer\": \"题目答案\"}";
    private static final String user_query1 = "MySQL";
    private static final String preset_context2 = "你现在是一名高级软件开发工程师，我接下来给你输入一道面试题和该面试题的用户回答，你给我生成打分、是否正确、参考答案和改正建议并按照以下json模板返回。" +
            "json模板：{\"userScore\": \"用户回答打分\", \"isCorrect\": \"true/false\", \"subjectAnswer\": \"参考答案\", \"suggestion\": \"改正建议\"}";
    private static final String user_query2 = "面试题：用c语言实现一个求和函数并返回求和结果；用户回答：int sum(int a, int b) { return a + b }";
    private static final String preset_context3 = "接下来我给你输入一篇文章，我帮我总结这篇文章，并提炼要点";
    private static final String html_content = "<p>Devv.ai 是一款面向程序员的新一代 AI 搜索引擎，旨在替代传统的搜索引擎和技术博客社区，专注于解决编程和技术难题。其主要特点包括：</p><ul><li><strong>AI 驱动</strong>：基于 RAG（检索增强生成）技术和大型语言模型，Devv.ai 能够提供快速、准确的编程相关查询结果。</li><li><strong>多语言支持</strong>：支持多种编程语言，如 Python、Go、JavaScript、Java 等。</li><li><strong>多种搜索模式</strong>：提供快速模式、代理模式和</li><li><strong>GitHub 模式</strong>：满足不同开发者的需求。 持续学习：基于用户互动不断学习和改进，提供更个性化的搜索体验。</li></ul>";
    @Resource
    DeepSeekService deepSeekService;

    @Test
    public void generateSubject() {
        long start = System.currentTimeMillis();
        // 构建 deepseek 请求对象
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", preset_context1),
                        new ChatRequest.Message("user", user_query1)
                ))
                .build();

        String json = deepSeekService.generateResponse(request);
        if (json.contains("```json")) {
            json = json.substring(json.lastIndexOf("```json") + 7, json.lastIndexOf("```"));
        }

        JSONObject json_obj = JSONUtil.parseObj(json);
        System.out.println("deepseek subjectName = " + json_obj.get("subjectName"));
        System.out.println("deepseek labelName = " + json_obj.get("labelName"));
        System.out.println("deepseek subjectAnswer = " + json_obj.get("subjectAnswer"));
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void generateSuggestion() {
        long start = System.currentTimeMillis();
        // 构建 deepseek 请求对象
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", preset_context2),
                        new ChatRequest.Message("user", user_query2)
                ))
                .build();

        String json = deepSeekService.generateResponse(request);
        if (json.contains("```json")) {
            json = json.substring(json.lastIndexOf("```json") + 7, json.lastIndexOf("```"));
        }

        JSONObject json_obj = JSONUtil.parseObj(json);
        System.out.println("deepseek userScore = " + json_obj.get("userScore"));
        System.out.println("deepseek isCorrect = " + json_obj.get("isCorrect"));
        System.out.println("deepseek subjectAnswer = " + json_obj.get("subjectAnswer"));
        System.out.println("deepseek suggestion = " + json_obj.get("suggestion"));
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void generateSummary() {
        long start = System.currentTimeMillis();
        // 构建 deepseek 请求对象
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", preset_context3),
                        new ChatRequest.Message("user", HtmlUtil.cleanHtmlTag(html_content))
                ))
                .build();
        System.out.println(deepSeekService.generateResponse(request));
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
    }

}
