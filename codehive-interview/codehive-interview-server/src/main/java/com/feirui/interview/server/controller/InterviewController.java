package com.feirui.interview.server.controller;

import com.alibaba.fastjson.JSON;
import com.feirui.interview.api.common.Result;
import com.feirui.interview.api.req.InterviewReq;
import com.feirui.interview.api.req.InterviewSubmitReq;
import com.feirui.interview.api.req.StartReq;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import com.feirui.interview.api.vo.InterviewResultVO;
import com.feirui.interview.api.vo.InterviewVO;
import com.feirui.interview.server.service.InterviewHistoryService;
import com.feirui.interview.server.service.InterviewService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 模拟面试信息 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/interview")
public class InterviewController {

    @Resource
    private InterviewService interviewService;
    @Resource
    private InterviewHistoryService interviewHistoryService;

    /**
     * 分析简历
     */
    @PostMapping(value = "/analyse")
    public Result<InterviewVO> analyse(@RequestBody InterviewReq req) {
        try {
            if (log.isInfoEnabled()) {
                log.info("分析简历入参{}", JSON.toJSON(req));
            }
            Preconditions.checkArgument(!Objects.isNull(req), "参数不能为空！");
            Preconditions.checkArgument(!Objects.isNull(req.getEngine()), "引擎不能为空！");
            Preconditions.checkArgument(!Objects.isNull(req.getUrl()), "简历不能为空！");
            return Result.ok(interviewService.analyse(req));
        } catch (IllegalArgumentException e) {
            log.error("参数异常！错误原因{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("分析简历异常！错误原因{}", e.getMessage(), e);
            return Result.fail("分析简历异常！");
        }
    }

    /**
     * 开始面试
     */
    @PostMapping(value = "/start")
    public Result<InterviewQuestionVO> start(@RequestBody StartReq req) {
        try {
            if (log.isInfoEnabled()) {
                log.info("开始面试入参{}", JSON.toJSON(req));
            }
            Preconditions.checkArgument(!Objects.isNull(req), "参数不能为空！");
            Preconditions.checkArgument(!Objects.isNull(req.getEngine()), "引擎不能为空！");
            Preconditions.checkArgument(!Objects.isNull(req.getQuestionList()), "关键字不能为空！");
            return Result.ok(interviewService.start(req));
        } catch (IllegalArgumentException e) {
            log.error("参数异常！错误原因{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("开始面试异常！错误原因{}", e.getMessage(), e);
            return Result.fail("开始面试异常！");
        }
    }

    /**
     * 面试提交答案
     */
    @PostMapping(value = "/submit")
    public Result<InterviewResultVO> submit(@RequestBody InterviewSubmitReq req) {
        try {
            if (log.isInfoEnabled()) {
                log.info("面试提交入参{}", JSON.toJSON(req));
            }
            Preconditions.checkArgument(!Objects.isNull(req), "参数不能为空！");
            InterviewResultVO submit = interviewService.submit(req);
            interviewHistoryService.logInterview(req, submit);
            return Result.ok(submit);
        } catch (IllegalArgumentException e) {
            log.error("参数异常！错误原因{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("分析简历异常！错误原因{}", e.getMessage(), e);
            return Result.fail("分析简历异常！");
        }
    }

}
