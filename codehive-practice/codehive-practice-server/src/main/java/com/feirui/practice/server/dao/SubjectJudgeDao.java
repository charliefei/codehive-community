package com.feirui.practice.server.dao;

import com.feirui.practice.server.entity.po.SubjectJudgePO;

public interface SubjectJudgeDao {

    SubjectJudgePO selectBySubjectId(Long repeatSubjectId);

}