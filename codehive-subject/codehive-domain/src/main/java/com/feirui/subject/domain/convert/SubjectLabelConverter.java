package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectLabelBO;
import com.feirui.subject.infra.basic.entity.SubjectLabel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface SubjectLabelConverter {
    SubjectLabelConverter INSTANCE = Mappers.getMapper(SubjectLabelConverter.class);

    SubjectLabel convert(SubjectLabelBO bo);

    List<SubjectLabelBO> convert(List<SubjectLabel> labelList);
}
