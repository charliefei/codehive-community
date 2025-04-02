package com.feirui.interview.server;

import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.feirui.ai.domain.ChatRequest;
import com.feirui.ai.service.DeepSeekService;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.feirui.ai.config.PresetPrompts.*;

@SpringBootTest
@Slf4j
public class ApplicationTest {

    private static final Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
    private static final String user_query2 = "面试题：用c语言实现一个求和函数并返回求和结果；用户回答：int sum(int a, int b) { return a + b }";
    private static final String html_content = "<p>Devv.ai 是一款面向程序员的新一代 AI 搜索引擎，旨在替代传统的搜索引擎和技术博客社区，专注于解决编程和技术难题。其主要特点包括：</p><ul><li><strong>AI 驱动</strong>：基于 RAG（检索增强生成）技术和大型语言模型，Devv.ai 能够提供快速、准确的编程相关查询结果。</li><li><strong>多语言支持</strong>：支持多种编程语言，如 Python、Go、JavaScript、Java 等。</li><li><strong>多种搜索模式</strong>：提供快速模式、代理模式和</li><li><strong>GitHub 模式</strong>：满足不同开发者的需求。 持续学习：基于用户互动不断学习和改进，提供更个性化的搜索体验。</li></ul>";
    @Resource
    DeepSeekService deepSeekService;

    @Test
    public void generateSubject() {
        long start = System.currentTimeMillis();
        String text;
        try(InputStream in = Files.newInputStream(Paths.get("/Desktop/resume.pdf"))) {
            PDDocument document = PDDocument.load(in);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage(Integer.MAX_VALUE);
            text = stripper.getText(document);
            text = pattern.matcher(text).replaceAll("");
            System.out.println(text);
            System.out.println("----------------------------");
        } catch (Exception e) {
            log.error("提取文本失败：", e);
            return;
        }
        // 构建 deepseek 请求对象
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", INTERVIEW_QUESTION_PROMPT),
                        new ChatRequest.Message("user", text)
                ))
                // .response_format(new ChatRequest.ResponseFormat("json_object"))
                .build();

        String json = deepSeekService.generateResponse(request);
        if (json.contains("```json")) {
            json = json.substring(json.lastIndexOf("```json") + 7, json.lastIndexOf("```"));
        }

        // JSONObject json_obj = JSONUtil.parseObj(json);
        // System.out.println("deepseek subjectName = " + json_obj.get("subjectName"));
        // System.out.println("deepseek labelName = " + json_obj.get("labelName"));
        // System.out.println("deepseek subjectAnswer = " + json_obj.get("subjectAnswer"));
        JSONArray json_arr = JSONUtil.parseArray(json);
        List<InterviewQuestionVO.Interview> list = json_arr.toList(InterviewQuestionVO.Interview.class);
        System.out.println("deepseek result = \n" + json_arr.toStringPretty());
        System.out.println("java result = \n" + list);
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void generateSuggestion() {
        long start = System.currentTimeMillis();
        // 构建 deepseek 请求对象
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", INTERVIEW_ANSWER_PROMPT),
                        new ChatRequest.Message("user", user_query2)
                ))
                .response_format(new ChatRequest.ResponseFormat("json_object"))
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
                        new ChatRequest.Message("system", CIRCLE_ARTICLE_SUMMARY_PROMPT),
                        new ChatRequest.Message("user", HtmlUtil.cleanHtmlTag(html_content))
                ))
                .build();
        System.out.println(deepSeekService.generateResponse(request));
        System.out.printf("耗时 %d 秒", (System.currentTimeMillis() - start) / 1000);
    }

}
