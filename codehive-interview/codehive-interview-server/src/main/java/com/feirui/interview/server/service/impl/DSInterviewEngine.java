package com.feirui.interview.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.feirui.ai.domain.ChatRequest;
import com.feirui.ai.service.DeepSeekService;
import com.feirui.interview.api.enums.EngineEnum;
import com.feirui.interview.api.req.InterviewSubmitReq;
import com.feirui.interview.api.req.StartReq;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import com.feirui.interview.api.vo.InterviewResultVO;
import com.feirui.interview.api.vo.InterviewVO;
import com.feirui.interview.server.service.InterviewEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.feirui.ai.config.PresetPrompts.INTERVIEW_ANSWER_PROMPT;
import static com.feirui.ai.config.PresetPrompts.INTERVIEW_QUESTION_PROMPT;

@Service
@Slf4j
public class DSInterviewEngine implements InterviewEngine {

    @Resource
    private DeepSeekService deepSeekService;

    @Override
    public EngineEnum engineType() {
        return EngineEnum.DEEPSEEK;
    }

    @Override
    public InterviewVO analyse(List<String> keyWords) {
        String pdfText = keyWords.remove(0);
        InterviewVO interviewVO = new InterviewVO();
        if (StringUtils.isBlank(pdfText) || CollectionUtil.isEmpty(keyWords)) {
            return interviewVO;
        }
        List<InterviewVO.Interview> questionList = keyWords.stream().map(item -> {
            InterviewVO.Interview interview = new InterviewVO.Interview();
            interview.setKeyWord(item);
            interview.setCategoryId(-1L);
            interview.setLabelId(-1L);
            return interview;
        }).collect(Collectors.toList());
        interviewVO.setPdfText(pdfText);
        interviewVO.setQuestionList(questionList);
        return interviewVO;
    }

    @Override
    public InterviewQuestionVO start(StartReq req) {
        long start = System.currentTimeMillis();
        String pdfText = req.getPdfText();
        InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
        if (StringUtils.isBlank(pdfText)) {
            return interviewQuestionVO;
        }
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", INTERVIEW_QUESTION_PROMPT),
                        new ChatRequest.Message("user", pdfText)
                ))
                .build();
        String json = deepSeekService.generateResponse(request);
        if (json.contains("```json")) {
            json = json.substring(json.lastIndexOf("```json") + 7, json.lastIndexOf("```"));
        }
        JSONArray jsonArray = JSONUtil.parseArray(json);
        log.info("cost: {}s data:\n{}", (System.currentTimeMillis() - start) / 1000, jsonArray.toStringPretty());
        List<InterviewQuestionVO.Interview> list = jsonArray.toList(InterviewQuestionVO.Interview.class);
        interviewQuestionVO.setQuestionList(list);
        return interviewQuestionVO;
    }

    @Override
    public InterviewResultVO submit(InterviewSubmitReq req) {
        long start = System.currentTimeMillis();
        List<InterviewSubmitReq.Submit> submits = req.getQuestionList();
        List<String> query = submits.stream().map(submit -> "面试题：" + submit.getSubjectName() + "。用户回答：" + submit.getUserAnswer()).collect(Collectors.toList());
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(Arrays.asList(
                        new ChatRequest.Message("system", INTERVIEW_ANSWER_PROMPT),
                        new ChatRequest.Message("user", JSONUtil.toJsonStr(query))
                ))
                .build();
        String json = deepSeekService.generateResponse(request);
        if (json.contains("```json")) {
            json = json.substring(json.lastIndexOf("```json") + 7, json.lastIndexOf("```"));
        }
        InterviewResultVO vo = JSONUtil.toBean(json, InterviewResultVO.class);
        log.info("cost: {}s data:\n{}", (System.currentTimeMillis() - start) / 1000, json);
        return vo;
    }

}
