package com.feirui.practice.server.service;

import com.feirui.practice.api.req.GetScoreDetailReq;
import com.feirui.practice.api.req.GetSubjectDetailReq;
import com.feirui.practice.api.req.SubmitPracticeDetailReq;
import com.feirui.practice.api.req.SubmitSubjectDetailReq;
import com.feirui.practice.api.vo.ScoreDetailVO;
import com.feirui.practice.api.vo.SubjectDetailVO;

import java.util.List;

public interface PracticeDetailService {

    /**
     * 练习提交题目
     */
    Boolean submitSubject(SubmitSubjectDetailReq req);

    /**
     * 提交练题情况
     */
    Boolean submit(SubmitPracticeDetailReq req);

    /**
     * 每题得分详情
     */
    List<ScoreDetailVO> getScoreDetail(GetScoreDetailReq req);

    /**
     * 获得答案详情
     */
    SubjectDetailVO getSubjectDetail(GetSubjectDetailReq req);

}