package com.feirui.subject.domain.service.impl;

import com.feirui.subject.common.enums.CategoryTypeEnum;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.domain.bo.SubjectLabelBO;
import com.feirui.subject.domain.convert.SubjectLabelConverter;
import com.feirui.subject.infra.basic.entity.SubjectCategory;
import com.feirui.subject.infra.basic.entity.SubjectLabel;
import com.feirui.subject.infra.basic.entity.SubjectMapping;
import com.feirui.subject.infra.basic.service.SubjectCategoryService;
import com.feirui.subject.infra.basic.service.SubjectLabelService;
import com.feirui.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectLabelDomainService {
    @Resource
    private SubjectLabelService subjectLabelService;
    @Resource
    private SubjectMappingService subjectMappingService;
    @Resource
    private SubjectCategoryService subjectCategoryService;

    public void add(SubjectLabelBO bo) {
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE.convert(bo);
        subjectLabel.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        subjectLabelService.insert(subjectLabel);
    }

    public void update(SubjectLabelBO bo) {
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE.convert(bo);
        subjectLabelService.update(subjectLabel);
    }

    public void delete(SubjectLabelBO bo) {
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE.convert(bo);
        subjectLabel.setIsDeleted(IsDeletedFlagEnum.DELETED.getStatus());
        subjectLabelService.update(subjectLabel);
    }

    public List<SubjectLabelBO> queryLabelByCategoryId(SubjectLabelBO bo) {
        Long categoryId = bo.getCategoryId();
        SubjectCategory subjectCategory = subjectCategoryService.queryById(categoryId);
        // 是一级分类就直接在标签表查询出结果返回
        if (Objects.equals(CategoryTypeEnum.PRIMARY.getType(), subjectCategory.getCategoryType())) {
            List<SubjectLabel> labelList = subjectLabelService
                    .queryLabelsByCategoryId(categoryId);
            return SubjectLabelConverter.INSTANCE
                    .convert(labelList);
        }
        // 是二级分类就在映射表中查询出标签id后再从标签表查询
        List<SubjectMapping> mappingList = subjectMappingService.queryMappingsByCategoryId(categoryId);
        if (CollectionUtils.isEmpty(mappingList)) {
            return Collections.emptyList();
        }
        List<Long> labelIdList = mappingList.stream()
                .map(SubjectMapping::getLabelId)
                .distinct()
                .collect(Collectors.toList());

        List<SubjectLabel> labelList = subjectLabelService.queryLabelsByIds(labelIdList);
        return SubjectLabelConverter.INSTANCE
                .convert(labelList);
    }
}
