package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.domain.bo.SubjectOptionBO;
import com.feirui.subject.infra.basic.entity.SubjectInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectInfoConverter {
    SubjectInfoConverter INSTANCE = Mappers.getMapper(SubjectInfoConverter.class);

    SubjectInfo convert(SubjectInfoBO bo);

    List<SubjectInfoBO> convert(List<SubjectInfo> subjectInfoList);

    SubjectInfoBO convert(SubjectOptionBO optionBO);
}
