package com.feirui.practice.server.dao;

import com.feirui.practice.server.entity.dto.CategoryDTO;
import com.feirui.practice.server.entity.po.SubjectCategoryPO;
import com.feirui.practice.server.entity.po.PrimaryCategoryPO;

import java.util.List;

public interface SubjectCategoryDao {

    List<PrimaryCategoryPO> getPrimaryCategory(CategoryDTO categoryDTO);

    SubjectCategoryPO getById(Long id);

    List<SubjectCategoryPO> list(CategoryDTO categoryDTO);

}
