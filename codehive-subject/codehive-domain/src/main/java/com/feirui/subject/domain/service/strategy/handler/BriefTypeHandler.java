package com.feirui.subject.domain.service.strategy.handler;

import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.common.enums.SubjectTypeEnum;
import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.domain.bo.SubjectOptionBO;
import com.feirui.subject.domain.convert.BriefSubjectConverter;
import com.feirui.subject.infra.basic.entity.SubjectBrief;
import com.feirui.subject.infra.basic.service.SubjectBriefService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 简答题策略类
 */
@Component
public class BriefTypeHandler implements SubjectTypeHandler {
    @Resource
    private SubjectBriefService subjectBriefService;

    @Override
    public SubjectTypeEnum getHandlerType() {
        return SubjectTypeEnum.BRIEF;
    }

    @Override
    public void add(SubjectInfoBO subjectInfoBO) {
        SubjectBrief subjectBrief = BriefSubjectConverter.INSTANCE.convert(subjectInfoBO);
        subjectBrief.setSubjectId(subjectInfoBO.getId().intValue());
        subjectBrief.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        subjectBriefService.save(subjectBrief);
    }

    @Override
    public SubjectOptionBO query(Long subjectId) {
        // 根据题目id查出简答题表数据
        SubjectBrief subjectBrief = subjectBriefService.queryBySubjectId(subjectId);
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        // 直接填入简答题答案即可
        subjectOptionBO.setSubjectAnswer(subjectBrief.getSubjectAnswer());
        return subjectOptionBO;
    }
}
