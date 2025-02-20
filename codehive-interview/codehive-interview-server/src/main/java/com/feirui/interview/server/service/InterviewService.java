package com.feirui.interview.server.service;

import com.feirui.interview.api.req.InterviewReq;
import com.feirui.interview.api.req.StartReq;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import com.feirui.interview.api.vo.InterviewVO;

public interface InterviewService {

    InterviewVO analyse(InterviewReq req);

    InterviewQuestionVO start(StartReq req);

}
