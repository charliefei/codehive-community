package com.feirui.interview.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.interview.api.enums.IsDeletedFlagEnum;
import com.feirui.interview.api.req.InterviewSubmitReq;
import com.feirui.interview.api.vo.InterviewResultVO;
import com.feirui.interview.server.config.context.LoginContextHolder;
import com.feirui.interview.server.dao.InterviewHistoryDao;
import com.feirui.interview.server.dao.InterviewQuestionHistoryDao;
import com.feirui.interview.server.entity.po.InterviewHistory;
import com.feirui.interview.server.entity.po.InterviewQuestionHistory;
import com.feirui.interview.server.service.InterviewHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 面试汇总记录表(InterviewHistory)表服务实现类
 */
@Service("interviewHistoryService")
public class InterviewHistoryServiceImpl extends ServiceImpl<InterviewHistoryDao, InterviewHistory> implements InterviewHistoryService {

    @Resource
    private InterviewHistoryDao interviewHistoryDao;
    @Resource
    private InterviewQuestionHistoryDao interviewQuestionHistoryDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logInterview(InterviewSubmitReq req, InterviewResultVO submit) {
        InterviewHistory history = new InterviewHistory();
        history.setAvgScore(submit.getAvgScore());
        String keyWords = req.getQuestionList().stream().map(InterviewSubmitReq.Submit::getLabelName).distinct().collect(Collectors.joining("、"));
        history.setKeyWords(keyWords);
        history.setTip(submit.getTips());
        history.setInterviewUrl(req.getInterviewUrl());
        history.setCreatedBy(LoginContextHolder.getLoginId());
        history.setCreatedTime(new Date());
        history.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.code);
        interviewHistoryDao.insert(history);
        List<InterviewQuestionHistory> histories = req.getQuestionList().stream().map(item -> {
            InterviewQuestionHistory questionHistory = new InterviewQuestionHistory();
            questionHistory.setInterviewId(history.getId());
            questionHistory.setScore(item.getUserScore());
            questionHistory.setKeyWords(item.getLabelName());
            questionHistory.setQuestion(item.getSubjectName());
            questionHistory.setAnswer(item.getSubjectAnswer());
            questionHistory.setUserAnswer(item.getUserAnswer());
            questionHistory.setCreatedBy(history.getCreatedBy());
            questionHistory.setCreatedTime(history.getCreatedTime());
            questionHistory.setIsDeleted(history.getIsDeleted());
            return questionHistory;
        }).collect(Collectors.toList());
        interviewQuestionHistoryDao.insertBatch(histories);
    }

}
