package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectAnswerBO;
import com.feirui.subject.infra.basic.entity.SubjectMultiple;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MultipleSubjectConverter {
    MultipleSubjectConverter INSTANCE = Mappers.getMapper(MultipleSubjectConverter.class);

    SubjectMultiple convert(SubjectAnswerBO subjectAnswerBO);

    List<SubjectAnswerBO> convert(List<SubjectMultiple> subjectMultipleList);
}
