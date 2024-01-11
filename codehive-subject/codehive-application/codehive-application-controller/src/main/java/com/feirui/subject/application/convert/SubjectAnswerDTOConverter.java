package com.feirui.subject.application.convert;

import com.feirui.subject.application.dto.SubjectAnswerDTO;
import com.feirui.subject.domain.bo.SubjectAnswerBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 题目信息dto转换器
 */
@Mapper
public interface SubjectAnswerDTOConverter {
    SubjectAnswerDTOConverter INSTANCE = Mappers.getMapper(SubjectAnswerDTOConverter.class);

    List<SubjectAnswerBO> convert(List<SubjectAnswerDTO> optionList);
}
