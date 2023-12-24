package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectCategoryBO;
import com.feirui.subject.infra.basic.entity.SubjectCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectCategoryConverter {
    SubjectCategoryConverter INSTANCE = Mappers.getMapper(SubjectCategoryConverter.class);

    SubjectCategory convert(SubjectCategoryBO subjectCategoryBO);

    List<SubjectCategoryBO> convert(List<SubjectCategory> categoryList);
}
