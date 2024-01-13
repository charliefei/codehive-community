package com.feirui.subject.domain.service.strategy.handler;

import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.common.enums.SubjectTypeEnum;
import com.feirui.subject.domain.bo.SubjectAnswerBO;
import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.domain.bo.SubjectOptionBO;
import com.feirui.subject.domain.convert.RadioSubjectConverter;
import com.feirui.subject.infra.basic.entity.SubjectRadio;
import com.feirui.subject.infra.basic.service.SubjectRadioService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单选策略类
 */
@Component
public class RadioTypeHandler implements SubjectTypeHandler {
    @Resource
    private SubjectRadioService subjectRadioService;

    @Override
    public SubjectTypeEnum getHandlerType() {
        return SubjectTypeEnum.RADIO;
    }

    @Override
    public void add(SubjectInfoBO subjectInfoBO) {
        //单选题目的插入
        List<SubjectRadio> subjectRadioList = subjectInfoBO.getOptionList()
                .stream()
                .map(option -> {
                    // 单选题答案：n个选项(1个正确、n-1个错误)：选项标识(ABC..)+选项是否正确+选项内容
                    // 相当于BeanUtils.copyProperties(source, target)
                    SubjectRadio subjectRadio = RadioSubjectConverter.INSTANCE.convert(option);
                    subjectRadio.setSubjectId(subjectInfoBO.getId());
                    subjectRadio.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
                    return subjectRadio;
                })
                .collect(Collectors.toList());
        subjectRadioService.saveBatch(subjectRadioList);
    }

    @Override
    public SubjectOptionBO query(Long subjectId) {
        // 查出单选题数据
        List<SubjectRadio> subjectRadios = subjectRadioService.getBySubjectId(subjectId);
        // 属性值复制到SubjectAnswer，再设置到SubjectOption上
        List<SubjectAnswerBO> subjectAnswerBOList = RadioSubjectConverter.INSTANCE.convert(subjectRadios);
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        subjectOptionBO.setOptionList(subjectAnswerBOList);
        return subjectOptionBO;
    }
}
