package com.feirui.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.domain.convert.SubjectCategoryConverter;
import com.feirui.subject.domain.bo.SubjectCategoryBO;
import com.feirui.subject.domain.service.SubjectCategoryDomainService;
import com.feirui.subject.infra.basic.entity.SubjectCategory;
import com.feirui.subject.infra.basic.service.SubjectCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SubjectCategoryDomainServiceImpl implements SubjectCategoryDomainService {
    @Resource
    private SubjectCategoryService subjectCategoryService;

    public void add(SubjectCategoryBO bo) {
        if (log.isInfoEnabled()) {
            log.info("add.bo {}", JSON.toJSONString(bo));
        }
        SubjectCategory category = SubjectCategoryConverter.INSTANCE
                .convert(bo);
        category.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        subjectCategoryService.insert(category);
    }

    @Override
    public List<SubjectCategoryBO> queryCategory(SubjectCategoryBO bo) {
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                        .convert(bo);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        List<SubjectCategory> categories = subjectCategoryService
                .queryCategory(subjectCategory);
        return SubjectCategoryConverter.INSTANCE.convert(categories);
    }

    @Override
    public Boolean update(SubjectCategoryBO bo) {
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convert(bo);
        Integer update = subjectCategoryService.update(subjectCategory);
        return update > 0;
    }

    @Override
    public Boolean delete(SubjectCategoryBO bo) {
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convert(bo);
        // 逻辑删除
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.DELETED.getStatus());
        Integer update = subjectCategoryService.update(subjectCategory);
        return update > 0;
    }
}
