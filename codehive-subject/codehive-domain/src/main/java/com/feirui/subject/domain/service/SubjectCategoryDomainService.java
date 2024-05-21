package com.feirui.subject.domain.service;

import com.feirui.subject.domain.bo.SubjectCategoryBO;

import java.util.List;

public interface SubjectCategoryDomainService {
    /**
     * 添加分类
     */
    void add(SubjectCategoryBO bo);

    /**
     * 查询分类
     */
    List<SubjectCategoryBO> queryCategory(SubjectCategoryBO bo);

    /**
     * 更新分类
     */
    Boolean update(SubjectCategoryBO bo);

    /**
     * 删除分类
     */
    Boolean delete(SubjectCategoryBO bo);

    /**
     * 查询分类及其标签信息
     */
    List<SubjectCategoryBO> queryCategoryAndLabel(SubjectCategoryBO subjectCategoryBO);
}
