package com.feirui.interview.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.interview.api.req.InterviewSubmitReq;
import com.feirui.interview.api.vo.InterviewResultVO;
import com.feirui.interview.server.entity.po.InterviewHistory;

/**
 * 面试汇总记录表(InterviewHistory)表服务接口
 */
public interface InterviewHistoryService extends IService<InterviewHistory> {

    void logInterview(InterviewSubmitReq req, InterviewResultVO submit);

}
