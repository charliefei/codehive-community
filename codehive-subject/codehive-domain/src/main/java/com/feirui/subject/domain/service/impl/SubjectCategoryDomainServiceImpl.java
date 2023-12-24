package com.feirui.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.feirui.subject.domain.convert.SubjectCategoryConverter;
import com.feirui.subject.domain.bo.SubjectCategoryBO;
import com.feirui.subject.domain.service.SubjectCategoryDomainService;
import com.feirui.subject.infra.basic.entity.SubjectCategory;
import com.feirui.subject.infra.basic.service.SubjectCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        subjectCategoryService.insert(category);
    }

    @Override
    public List<SubjectCategoryBO> queryCategory(SubjectCategoryBO bo) {
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                        .convert(bo);
        subjectCategory.setIsDeleted(0);
        List<SubjectCategory> categories = subjectCategoryService
                .queryCategory(subjectCategory);
        return SubjectCategoryConverter.INSTANCE.convert(categories);
    }
}
