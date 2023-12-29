package com.feirui.subject.domain.service.impl;

import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.domain.bo.SubjectLabelBO;
import com.feirui.subject.domain.convert.SubjectLabelConverter;
import com.feirui.subject.infra.basic.entity.SubjectLabel;
import com.feirui.subject.infra.basic.entity.SubjectMapping;
import com.feirui.subject.infra.basic.service.SubjectLabelService;
import com.feirui.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectLabelDomainService {
    @Resource
    private SubjectLabelService subjectLabelService;
    @Resource
    private SubjectMappingService subjectMappingService;

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
        List<SubjectMapping> mappingList = subjectMappingService.queryMappingsByCategoryId(categoryId);
        if (CollectionUtils.isEmpty(mappingList)) {
            return Collections.emptyList();
        }
        List<Long> labelIdList = mappingList.stream()
                .map(SubjectMapping::getLabelId)
                .distinct()
                .collect(Collectors.toList());

        List<SubjectLabel> labelList = subjectLabelService.queryLabelsByIds(labelIdList);
        return labelList.stream().map(label -> {
            SubjectLabelBO subjectLabelBO = new SubjectLabelBO();
            subjectLabelBO.setId(label.getId());
            subjectLabelBO.setSortNum(label.getSortNum());
            subjectLabelBO.setLabelName(label.getLabelName());
            subjectLabelBO.setCategoryId(categoryId);
            return subjectLabelBO;
        }).collect(Collectors.toList());
    }
}
