package com.feirui.subject.application.convert;

import com.feirui.subject.application.dto.SubjectLabelDTO;
import com.feirui.subject.domain.bo.SubjectLabelBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectLabelDTOConverter {
    SubjectLabelDTOConverter INSTANCE = Mappers.getMapper(SubjectLabelDTOConverter.class);

    SubjectLabelBO convert(SubjectLabelDTO subjectLabelDTO);

    List<SubjectLabelDTO> convert(List<SubjectLabelBO> subjectLabelBOList);
}
