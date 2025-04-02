package com.feirui.interview.server.service;

import com.feirui.interview.api.req.InterviewReq;
import com.feirui.interview.api.req.InterviewSubmitReq;
import com.feirui.interview.api.req.StartReq;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import com.feirui.interview.api.vo.InterviewResultVO;
import com.feirui.interview.api.vo.InterviewVO;

public interface InterviewService {

    InterviewVO analyse(InterviewReq req);
    InterviewVO analyseV2(InterviewReq req);

    InterviewQuestionVO start(StartReq req);

    InterviewResultVO submit(InterviewSubmitReq req);

}
