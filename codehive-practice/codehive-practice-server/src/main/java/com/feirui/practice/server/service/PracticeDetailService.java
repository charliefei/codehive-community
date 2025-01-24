package com.feirui.practice.server.service;

import com.feirui.practice.api.req.SubmitPracticeDetailReq;

public interface PracticeDetailService {

    /**
     * 提交练题情况
     */
    Boolean submit(SubmitPracticeDetailReq req);

}