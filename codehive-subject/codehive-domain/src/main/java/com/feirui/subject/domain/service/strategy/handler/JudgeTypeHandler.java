package com.feirui.subject.domain.service.strategy.handler;

import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.common.enums.SubjectTypeEnum;
import com.feirui.subject.domain.bo.SubjectAnswerBO;
import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.domain.bo.SubjectOptionBO;
import com.feirui.subject.domain.convert.JudgeSubjectConverter;
import com.feirui.subject.infra.basic.entity.SubjectJudge;
import com.feirui.subject.infra.basic.service.SubjectJudgeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 判断题策略类
 */
@Component
public class JudgeTypeHandler implements SubjectTypeHandler {
    @Resource
    private SubjectJudgeService subjectJudgeService;

    @Override
    public SubjectTypeEnum getHandlerType() {
        return SubjectTypeEnum.JUDGE;
    }

    @Override
    public void add(SubjectInfoBO subjectInfoBO) {
        // 判断题目的插入
        SubjectJudge subjectJudge = new SubjectJudge();
        // 判断题答案：只需要isCorrect标识对错即可
        SubjectAnswerBO subjectAnswerBO = subjectInfoBO.getOptionList().get(0);
        subjectJudge.setSubjectId(subjectInfoBO.getId());
        subjectJudge.setIsCorrect(subjectAnswerBO.getIsCorrect());
        subjectJudge.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        subjectJudgeService.save(subjectJudge);
    }

    @Override
    public SubjectOptionBO query(Long subjectId) {
        // 根据题目id查出判断题答案数据
        List<SubjectJudge> subjectJudges = subjectJudgeService.getBySubjectId(subjectId);
        // 属性值复制到SubjectAnswer，再设置到SubjectOption上
        List<SubjectAnswerBO> subjectAnswerBOList = JudgeSubjectConverter.INSTANCE
                .convert(subjectJudges);
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        subjectOptionBO.setOptionList(subjectAnswerBOList);
        return subjectOptionBO;
    }
}
