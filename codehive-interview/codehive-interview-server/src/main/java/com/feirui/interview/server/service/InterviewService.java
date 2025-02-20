package com.feirui.interview.server.service;

import com.feirui.interview.api.req.InterviewReq;
import com.feirui.interview.api.vo.InterviewVO;

public interface InterviewService {

    InterviewVO analyse(InterviewReq req);

}
