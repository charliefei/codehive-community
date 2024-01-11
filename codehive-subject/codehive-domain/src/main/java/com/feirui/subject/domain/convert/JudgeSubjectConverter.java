package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectAnswerBO;
import com.feirui.subject.infra.basic.entity.SubjectJudge;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface JudgeSubjectConverter {
    JudgeSubjectConverter INSTANCE = Mappers.getMapper(JudgeSubjectConverter.class);

    List<SubjectAnswerBO> convert(List<SubjectJudge> subjectJudgeList);
}
