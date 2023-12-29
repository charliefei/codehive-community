package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectLabelBO;
import com.feirui.subject.infra.basic.entity.SubjectLabel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface SubjectLabelConverter {
    SubjectLabelConverter INSTANCE = Mappers.getMapper(SubjectLabelConverter.class);

    SubjectLabel convert(SubjectLabelBO bo);
}
