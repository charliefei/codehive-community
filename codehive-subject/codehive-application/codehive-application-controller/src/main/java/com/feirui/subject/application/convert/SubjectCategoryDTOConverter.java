package com.feirui.subject.application.convert;

import com.feirui.subject.application.dto.SubjectCategoryDTO;
import com.feirui.subject.domain.bo.SubjectCategoryBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectCategoryDTOConverter {
    SubjectCategoryDTOConverter INSTANCE = Mappers.getMapper(SubjectCategoryDTOConverter.class);

    SubjectCategoryBO convert(SubjectCategoryDTO subjectCategoryDTO);

    List<SubjectCategoryDTO> convert(List<SubjectCategoryBO> bos);

    SubjectCategoryDTO convert(SubjectCategoryBO bo);
}
