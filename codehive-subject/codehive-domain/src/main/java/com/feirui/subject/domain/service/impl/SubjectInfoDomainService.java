package com.feirui.subject.domain.service.impl;

import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.domain.convert.SubjectInfoConverter;
import com.feirui.subject.domain.service.strategy.SubjectTypeHandlerFactory;
import com.feirui.subject.domain.service.strategy.handler.SubjectTypeHandler;
import com.feirui.subject.infra.basic.entity.SubjectInfo;
import com.feirui.subject.infra.basic.entity.SubjectMapping;
import com.feirui.subject.infra.basic.service.SubjectInfoService;
import com.feirui.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class SubjectInfoDomainService {
    @Resource
    private SubjectInfoService subjectInfoService;
    @Resource
    private SubjectTypeHandlerFactory subjectTypeHandlerFactory;
    @Resource
    private SubjectMappingService subjectMappingService;

    public void add(SubjectInfoBO bo) {
        SubjectInfo subjectInfo = SubjectInfoConverter.INSTANCE.convert(bo);
        subjectInfo.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        // 存储公共信息
        subjectInfoService.save(subjectInfo);

        // 不同类型题目交给不同策略类存储特有信息
        SubjectTypeHandler handler = subjectTypeHandlerFactory.getHandler(subjectInfo.getSubjectType());
        bo.setId(subjectInfo.getId());
        handler.add(bo);

        // 存储题目分类、题目标签、题目三者的映射关系
        List<Integer> categoryIds = bo.getCategoryIds();
        List<Integer> labelIds = bo.getLabelIds();
        List<SubjectMapping> subjectMappingList = new LinkedList<>();
        categoryIds.forEach(categoryId -> {
            labelIds.forEach(labelId -> {
                SubjectMapping subjectMapping = new SubjectMapping();
                subjectMapping.setSubjectId(subjectInfo.getId());
                subjectMapping.setCategoryId(Long.valueOf(categoryId));
                subjectMapping.setLabelId(Long.valueOf(labelId));
                subjectMapping.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
                subjectMappingList.add(subjectMapping);
            });
        });
        subjectMappingService.saveBatch(subjectMappingList);
    }
}
