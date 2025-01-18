package com.feirui.practice.server.service;

import com.feirui.practice.api.vo.PracticeSetVO;
import com.feirui.practice.api.vo.SpecialPracticeVO;
import com.feirui.practice.server.entity.dto.PracticeSubjectDTO;

import java.util.List;

public interface PracticeSetService {

    /**
     * 获取专项练习内容
     */
    List<SpecialPracticeVO> getSpecialPracticeContent();

    /**
     * 开始练习
     */
    PracticeSetVO addPractice(PracticeSubjectDTO dto);
}
