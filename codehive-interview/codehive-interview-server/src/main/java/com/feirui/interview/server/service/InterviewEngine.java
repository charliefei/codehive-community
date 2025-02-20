package com.feirui.interview.server.service;

import com.feirui.interview.api.enums.EngineEnum;
import com.feirui.interview.api.req.InterviewSubmitReq;
import com.feirui.interview.api.req.StartReq;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import com.feirui.interview.api.vo.InterviewResultVO;
import com.feirui.interview.api.vo.InterviewVO;

import java.util.List;

/**
 * 面试引擎
 */
public interface InterviewEngine {

    /**
     * 引擎类型
     */
    EngineEnum engineType();

    /**
     * 通过简历关键字获取面试关键字
     */
    InterviewVO analyse(List<String> KeyWords);

    /**
     * 通过面试关键字获取面试题
     */
    InterviewQuestionVO start(StartReq req);

    /**
     * 提交面试题
     */
    InterviewResultVO submit(InterviewSubmitReq req);

}
