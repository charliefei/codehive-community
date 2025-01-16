package com.feirui.practice.server.service.impl;

import com.feirui.practice.api.enums.SubjectTypeEnum;
import com.feirui.practice.api.vo.SpecialPracticeCategoryVO;
import com.feirui.practice.api.vo.SpecialPracticeLabelVO;
import com.feirui.practice.api.vo.SpecialPracticeVO;
import com.feirui.practice.server.dao.SubjectCategoryDao;
import com.feirui.practice.server.dao.SubjectLabelDao;
import com.feirui.practice.server.dao.SubjectMappingDao;
import com.feirui.practice.server.entity.dto.CategoryDTO;
import com.feirui.practice.server.entity.po.LabelCountPO;
import com.feirui.practice.server.entity.po.PrimaryCategoryPO;
import com.feirui.practice.server.entity.po.SubjectCategoryPO;
import com.feirui.practice.server.entity.po.SubjectLabelPO;
import com.feirui.practice.server.service.PracticeSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class PracticeSetServiceImpl implements PracticeSetService {

    @Resource
    private SubjectCategoryDao subjectCategoryDao;
    @Resource
    private SubjectMappingDao subjectMappingDao;
    @Resource
    private SubjectLabelDao subjectLabelDao;

    @Override
    public List<SpecialPracticeVO> getSpecialPracticeContent() {
        List<Integer> subjectTypeList = Arrays.asList(
                SubjectTypeEnum.RADIO.getCode(),
                SubjectTypeEnum.MULTIPLE.getCode(),
                SubjectTypeEnum.JUDGE.getCode());
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSubjectTypeList(subjectTypeList);
        List<PrimaryCategoryPO> primaryCategoryPOList = subjectCategoryDao.getPrimaryCategory(categoryDTO);
        if (CollectionUtils.isEmpty(primaryCategoryPOList)) {
            return Collections.emptyList();
        }

        List<SpecialPracticeVO> result = new ArrayList<>();
        primaryCategoryPOList.forEach(primaryCategoryPO -> {
            SpecialPracticeVO specialPracticeVO = new SpecialPracticeVO();
            specialPracticeVO.setPrimaryCategoryId(primaryCategoryPO.getParentId());
            SubjectCategoryPO primaryCategory = subjectCategoryDao.getById(primaryCategoryPO.getParentId());
            specialPracticeVO.setPrimaryCategoryName(primaryCategory.getCategoryName());

            CategoryDTO condition = new CategoryDTO();
            condition.setCategoryType(2);
            condition.setParentId(primaryCategoryPO.getParentId());
            List<SubjectCategoryPO> childCategoryList = subjectCategoryDao.list(condition);
            if (CollectionUtils.isEmpty(childCategoryList)) {
                // 大分类下没有子分类，不做展示
                return;
            }
            List<SpecialPracticeCategoryVO> specialPracticeCategoryVOList = new ArrayList<>();
            childCategoryList.forEach(childCategory -> {
                List<SpecialPracticeLabelVO> specialPracticeLabelVOList = getLabelVOList(
                        childCategory.getId(),
                        subjectTypeList);
                if (CollectionUtils.isEmpty(specialPracticeLabelVOList)) {
                    // 子分类下没有标签，不做展示
                    return;
                }
                SpecialPracticeCategoryVO specialPracticeCategoryVO = new SpecialPracticeCategoryVO();
                specialPracticeCategoryVO.setCategoryId(childCategory.getId());
                specialPracticeCategoryVO.setCategoryName(childCategory.getCategoryName());
                specialPracticeCategoryVO.setLabelList(specialPracticeLabelVOList);
                specialPracticeCategoryVOList.add(specialPracticeCategoryVO);
            });
            specialPracticeVO.setCategoryList(specialPracticeCategoryVOList);
            result.add(specialPracticeVO);
        });
        return result;
    }

    /**
     * 获取分类下的标签
     *
     * @param categoryId      分类id
     * @param subjectTypeList 题型
     */
    private List<SpecialPracticeLabelVO> getLabelVOList(Long categoryId, List<Integer> subjectTypeList) {
        List<LabelCountPO> countPOList = subjectMappingDao.getLabelSubjectCount(categoryId, subjectTypeList);
        if (CollectionUtils.isEmpty(countPOList)) {
            return Collections.emptyList();
        }
        List<SpecialPracticeLabelVO> specialPracticeLabelVOList = new LinkedList<>();
        countPOList.forEach(countPo -> {
            SpecialPracticeLabelVO specialPracticeLabelVO = new SpecialPracticeLabelVO();
            specialPracticeLabelVO.setId(countPo.getLabelId());
            specialPracticeLabelVO.setAssembleId(categoryId + "-" + countPo.getLabelId());
            SubjectLabelPO subjectLabelPO = subjectLabelDao.queryById(countPo.getLabelId());
            specialPracticeLabelVO.setLabelName(subjectLabelPO.getLabelName());
            specialPracticeLabelVOList.add(specialPracticeLabelVO);
        });
        return specialPracticeLabelVOList;
    }

}
