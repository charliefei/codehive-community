package com.feirui.subject.domain.convert;

import com.feirui.subject.domain.bo.SubjectLikedBO;
import com.feirui.subject.infra.basic.entity.SubjectLiked;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectLikedBOConverter {

    SubjectLikedBOConverter INSTANCE = Mappers.getMapper(SubjectLikedBOConverter.class);

    SubjectLiked convert(SubjectLikedBO subjectLikedBO);

    List<SubjectLikedBO> convert(List<SubjectLiked> subjectLikedList);
}