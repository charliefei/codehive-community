package com.feirui.subject.domain.service.strategy.handler;

import com.feirui.subject.common.enums.SubjectTypeEnum;
import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.domain.bo.SubjectOptionBO;

public interface SubjectTypeHandler {
    /**
     * 枚举身份的识别
     */
    SubjectTypeEnum getHandlerType();

    /**
     * 实际的题目的插入
     */
    void add(SubjectInfoBO subjectInfoBO);

    SubjectOptionBO query(Long subjectId);
}
