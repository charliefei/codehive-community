package com.feirui.subject.application.convert;

import com.feirui.subject.application.dto.SubjectInfoDTO;
import com.feirui.subject.domain.bo.SubjectInfoBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 题目信息dto转换器
 */
@Mapper
public interface SubjectInfoDTOConverter {
    SubjectInfoDTOConverter INSTANCE = Mappers.getMapper(SubjectInfoDTOConverter.class);

    SubjectInfoBO convert(SubjectInfoDTO dto);
}
