package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectAnswerBO;
import com.feirui.subject.infra.basic.entity.SubjectRadio;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RadioSubjectConverter {
    RadioSubjectConverter INSTANCE = Mappers.getMapper(RadioSubjectConverter.class);

    SubjectRadio convert(SubjectAnswerBO subjectAnswerBO);

    List<SubjectAnswerBO> convert(List<SubjectRadio> subjectRadioList);
}
