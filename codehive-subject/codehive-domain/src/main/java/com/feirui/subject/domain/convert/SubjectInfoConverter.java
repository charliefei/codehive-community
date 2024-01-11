package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.infra.basic.entity.SubjectInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectInfoConverter {
    SubjectInfoConverter INSTANCE = Mappers.getMapper(SubjectInfoConverter.class);

    SubjectInfo convert(SubjectInfoBO bo);
}
