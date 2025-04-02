package com.feirui.interview.server.service.impl;

import com.feirui.interview.api.enums.EngineEnum;
import com.feirui.interview.api.req.InterviewSubmitReq;
import com.feirui.interview.api.req.StartReq;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import com.feirui.interview.api.vo.InterviewResultVO;
import com.feirui.interview.api.vo.InterviewVO;
import com.feirui.interview.server.service.InterviewEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DSInterviewEngine implements InterviewEngine {

    @Override
    public EngineEnum engineType() {
        return EngineEnum.DEEPSEEK;
    }

    @Override
    public InterviewVO analyse(List<String> KeyWords) {
        return null;
    }

    @Override
    public InterviewQuestionVO start(StartReq req) {
        return null;
    }

    @Override
    public InterviewResultVO submit(InterviewSubmitReq req) {
        return null;
    }

}
