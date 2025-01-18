package com.feirui.practice.server.dao;

import com.feirui.practice.server.entity.dto.PracticeSubjectDTO;
import com.feirui.practice.server.entity.po.SubjectPO;

import java.util.List;

public interface SubjectDao {

    /**
     * 获取练习面试题目
     */
    List<SubjectPO> getPracticeSubject(PracticeSubjectDTO dto);

}