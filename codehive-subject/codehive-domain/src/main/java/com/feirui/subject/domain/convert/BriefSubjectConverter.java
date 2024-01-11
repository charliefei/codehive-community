package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.infra.basic.entity.SubjectBrief;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BriefSubjectConverter {
    BriefSubjectConverter INSTANCE = Mappers.getMapper(BriefSubjectConverter.class);

    SubjectBrief convert(SubjectInfoBO subjectInfoBO);
}
