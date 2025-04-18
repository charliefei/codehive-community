package com.feirui.practice.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.feirui.practice.api.common.PageInfo;
import com.feirui.practice.api.common.PageResult;
import com.feirui.practice.api.enums.CompleteStatusEnum;
import com.feirui.practice.api.enums.IsDeletedFlagEnum;
import com.feirui.practice.api.enums.SubjectTypeEnum;
import com.feirui.practice.api.req.GetPracticeSubjectsReq;
import com.feirui.practice.api.req.GetUnCompletePracticeReq;
import com.feirui.practice.api.vo.*;
import com.feirui.practice.server.config.context.LoginContextHolder;
import com.feirui.practice.server.dao.*;
import com.feirui.practice.server.entity.dto.CategoryDTO;
import com.feirui.practice.server.entity.dto.PracticeSetDTO;
import com.feirui.practice.server.entity.dto.PracticeSubjectDTO;
import com.feirui.practice.server.entity.po.*;
import com.feirui.practice.server.service.PracticeSetService;
import com.feirui.practice.server.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Resource
    private PracticeSetDetailDao practiceSetDetailDao;
    @Resource
    private PracticeSetDao practiceSetDao;
    @Resource
    private SubjectDao subjectDao;
    @Resource
    private PracticeDetailDao practiceDetailDao;
    @Resource
    private PracticeDao practiceDao;
    @Resource
    private SubjectRadioDao subjectRadioDao;
    @Resource
    private SubjectMultipleDao subjectMultipleDao;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PracticeSetVO addPractice(PracticeSubjectDTO dto) {
        PracticeSetVO setVO = new PracticeSetVO();
        List<PracticeSubjectDetailVO> practiceList = getPracticeList(dto);
        if (CollectionUtils.isEmpty(practiceList)) {
            return setVO;
        }
        PracticeSetPO practiceSetPO = new PracticeSetPO();
        // 实时生成的套卷
        practiceSetPO.setSetType(1);

        // 生成套卷名称：拼接小类的名称
        List<String> assembleIds = dto.getAssembleIds();
        Set<Long> categoryIdSet = new HashSet<>();
        assembleIds.forEach(assembleId -> {
            Long categoryId = Long.valueOf(assembleId.split("-")[0]);
            categoryIdSet.add(categoryId);
        });
        StringBuilder setName = new StringBuilder();
        int i = 1;
        for (Long categoryId : categoryIdSet) {
            if (i > 2) {
                break;
            }
            SubjectCategoryPO categoryPO = subjectCategoryDao.getById(categoryId);
            setName.append(categoryPO.getCategoryName());
            setName.append("、");
            i = i + 1;
        }
        setName.deleteCharAt(setName.length() - 1);
        if (i == 2) {
            setName.append("专项练习");
        } else {
            setName.append("等专项练习");
        }
        practiceSetPO.setSetName(setName.toString());

        String labelId = assembleIds.get(0).split("-")[1];
        SubjectLabelPO labelPO = subjectLabelDao.queryById(Long.valueOf(labelId));
        practiceSetPO.setPrimaryCategoryId(labelPO.getCategoryId());
        practiceSetPO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        practiceSetPO.setCreatedBy(LoginContextHolder.getLoginId());
        practiceSetPO.setCreatedTime(new Date());
        // 持久化到练习套卷表，保存了套卷的基本信息
        practiceSetDao.add(practiceSetPO);
        Long practiceSetId = practiceSetPO.getId();

        practiceList.forEach(e -> {
            PracticeSetDetailPO detailPO = new PracticeSetDetailPO();
            detailPO.setSetId(practiceSetId);
            detailPO.setSubjectId(e.getSubjectId());
            detailPO.setSubjectType(e.getSubjectType());
            detailPO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
            detailPO.setCreatedBy(LoginContextHolder.getLoginId());
            detailPO.setCreatedTime(new Date());
            // 持久化到练习套卷明细表，保存了套卷内的每一题，一对多
            practiceSetDetailDao.add(detailPO);
        });
        setVO.setSetId(practiceSetId);
        return setVO;
    }

    /**
     * 获取套卷题目信息
     */
    private List<PracticeSubjectDetailVO> getPracticeList(PracticeSubjectDTO dto) {
        List<PracticeSubjectDetailVO> practiceSubjectListVOS = new LinkedList<>();
        // 避免重复
        List<Long> excludeSubjectIds = new LinkedList<>();

        // 设置题目数量，之后优化到nacos动态配置
        Integer radioSubjectCount = 10;
        Integer multipleSubjectCount = 6;
        Integer judgeSubjectCount = 4;
        int totalSubjectCount = 20;
        // 查询单选
        dto.setSubjectCount(radioSubjectCount);
        dto.setSubjectType(SubjectTypeEnum.RADIO.getCode());
        assembleList(dto, practiceSubjectListVOS, excludeSubjectIds);
        // 查询多选
        dto.setSubjectCount(multipleSubjectCount);
        dto.setSubjectType(SubjectTypeEnum.MULTIPLE.getCode());
        assembleList(dto, practiceSubjectListVOS, excludeSubjectIds);
        // 查询判断
        dto.setSubjectCount(judgeSubjectCount);
        dto.setSubjectType(SubjectTypeEnum.JUDGE.getCode());
        assembleList(dto, practiceSubjectListVOS, excludeSubjectIds);
        // 补充题目
        if (practiceSubjectListVOS.size() == totalSubjectCount) {
            return practiceSubjectListVOS;
        }
        Integer remainCount = totalSubjectCount - practiceSubjectListVOS.size();
        dto.setSubjectCount(remainCount);
        dto.setSubjectType(1);
        assembleList(dto, practiceSubjectListVOS, excludeSubjectIds);
        return practiceSubjectListVOS;
    }

    private void assembleList(PracticeSubjectDTO dto, List<PracticeSubjectDetailVO> list, List<Long> excludeSubjectIds) {
        dto.setExcludeSubjectIds(excludeSubjectIds);
        List<SubjectPO> subjectPOList = subjectDao.getPracticeSubject(dto);
        if (CollectionUtils.isEmpty(subjectPOList)) {
            return;
        }
        subjectPOList.forEach(e -> {
            PracticeSubjectDetailVO vo = new PracticeSubjectDetailVO();
            vo.setSubjectId(e.getId());
            vo.setSubjectType(e.getSubjectType());
            excludeSubjectIds.add(e.getId());
            list.add(vo);
        });
    }

    @Override
    public PracticeSubjectListVO getSubjects(GetPracticeSubjectsReq req) {
        Long setId = req.getSetId();
        PracticeSubjectListVO vo = new PracticeSubjectListVO();
        List<PracticeSubjectDetailVO> practiceSubjectListVOS = new LinkedList<>();
        List<PracticeSetDetailPO> practiceSetDetailPOS = practiceSetDetailDao.selectBySetId(setId);
        if (CollectionUtils.isEmpty(practiceSetDetailPOS)) {
            return vo;
        }
        String loginId = LoginContextHolder.getLoginId();
        Long practiceId = req.getPracticeId();
        practiceSetDetailPOS.forEach(e -> {
            PracticeSubjectDetailVO practiceSubjectListVO = new PracticeSubjectDetailVO();
            practiceSubjectListVO.setSubjectId(e.getSubjectId());
            practiceSubjectListVO.setSubjectType(e.getSubjectType());
            if (Objects.nonNull(practiceId)) {
                PracticeDetailPO practiceDetailPO = practiceDetailDao.selectDetail(practiceId, e.getSubjectId(), loginId);
                if (Objects.nonNull(practiceDetailPO) && StringUtils.isNotBlank(practiceDetailPO.getAnswerContent())) {
                    practiceSubjectListVO.setIsAnswer(1);
                    practiceSubjectListVO.setAnswerContent(practiceDetailPO.getAnswerContent());
                } else {
                    practiceSubjectListVO.setIsAnswer(0);
                }
            }
            practiceSubjectListVOS.add(practiceSubjectListVO);
        });
        vo.setSubjectList(practiceSubjectListVOS);
        PracticeSetPO practiceSetPO = practiceSetDao.selectById(setId);
        vo.setTitle(practiceSetPO.getSetName());
        if (Objects.isNull(practiceId)) {
            Long newPracticeId = insertUnCompletePractice(setId);
            vo.setPracticeId(newPracticeId);
        } else {
            updateUnCompletePractice(practiceId);
            PracticePO practicePO = practiceDao.selectById(practiceId);
            vo.setTimeUse(practicePO.getTimeUse());
            vo.setPracticeId(practiceId);
        }
        return vo;
    }

    private Long insertUnCompletePractice(Long practiceSetId) {
        PracticePO practicePO = new PracticePO();
        practicePO.setSetId(practiceSetId);
        practicePO.setCompleteStatus(CompleteStatusEnum.NO_COMPLETE.getCode());
        practicePO.setTimeUse("00:00:00");
        practicePO.setSubmitTime(new Date());
        practicePO.setCorrectRate(new BigDecimal("0.00"));
        practicePO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        practicePO.setCreatedBy(LoginContextHolder.getLoginId());
        practicePO.setCreatedTime(new Date());
        practiceDao.insert(practicePO);
        return practicePO.getId();
    }

    private void updateUnCompletePractice(Long practiceId) {
        PracticePO practicePO = new PracticePO();
        practicePO.setId(practiceId);
        practicePO.setSubmitTime(new Date());
        practiceDao.update(practicePO);
    }

    @Override
    public PracticeSubjectVO getPracticeSubject(PracticeSubjectDTO dto) {
        PracticeSubjectVO practiceSubjectVO = new PracticeSubjectVO();
        SubjectPO subjectPO = subjectDao.selectById(dto.getSubjectId());
        practiceSubjectVO.setSubjectName(subjectPO.getSubjectName());
        practiceSubjectVO.setSubjectType(subjectPO.getSubjectType());
        if (dto.getSubjectType() == SubjectTypeEnum.RADIO.getCode()) {
            List<PracticeSubjectOptionVO> optionList = new LinkedList<>();
            List<SubjectRadioPO> radioSubjectPOS = subjectRadioDao.selectBySubjectId(subjectPO.getId());
            radioSubjectPOS.forEach(e -> {
                PracticeSubjectOptionVO practiceSubjectOptionVO = new PracticeSubjectOptionVO();
                practiceSubjectOptionVO.setOptionContent(e.getOptionContent());
                practiceSubjectOptionVO.setOptionType(e.getOptionType());
                optionList.add(practiceSubjectOptionVO);
            });
            practiceSubjectVO.setOptionList(optionList);
        }
        if (dto.getSubjectType() == SubjectTypeEnum.MULTIPLE.getCode()) {
            List<PracticeSubjectOptionVO> optionList = new LinkedList<>();
            List<SubjectMultiplePO> multipleSubjectPOS = subjectMultipleDao.selectBySubjectId(subjectPO.getId());
            multipleSubjectPOS.forEach(e -> {
                PracticeSubjectOptionVO practiceSubjectOptionVO = new PracticeSubjectOptionVO();
                practiceSubjectOptionVO.setOptionContent(e.getOptionContent());
                practiceSubjectOptionVO.setOptionType(e.getOptionType());
                optionList.add(practiceSubjectOptionVO);
            });
            practiceSubjectVO.setOptionList(optionList);
        }
        return practiceSubjectVO;
    }

    @Override
    public PageResult<PracticeSetVO> getPreSetContent(PracticeSetDTO dto) {
        PageResult<PracticeSetVO> pageResult = new PageResult<>();
        PageInfo pageInfo = dto.getPageInfo();
        pageResult.setPageNo(pageInfo.getPageNo());
        pageResult.setPageSize(pageInfo.getPageSize());
        int start = (pageInfo.getPageNo() - 1) * pageInfo.getPageSize();
        Integer count = practiceSetDao.getListCount(dto);
        if (count == 0) {
            return pageResult;
        }
        List<PracticeSetPO> setPOList = practiceSetDao.getSetList(dto, start, dto.getPageInfo().getPageSize());
        if (log.isInfoEnabled()) {
            log.info("获取的模拟考卷列表{}", JSON.toJSONString(setPOList));
        }
        List<PracticeSetVO> list = new LinkedList<>();
        setPOList.forEach(e -> {
            PracticeSetVO vo = new PracticeSetVO();
            vo.setSetId(e.getId());
            vo.setSetName(e.getSetName());
            vo.setSetHeat(e.getSetHeat());
            vo.setSetDesc(e.getSetDesc());
            list.add(vo);
        });
        pageResult.setRecords(list);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public PageResult<UnCompletePracticeSetVO> getUnCompletePractice(GetUnCompletePracticeReq req) {
        PageResult<UnCompletePracticeSetVO> pageResult = new PageResult<>();
        PageInfo pageInfo = req.getPageInfo();
        pageResult.setPageNo(pageInfo.getPageNo());
        pageResult.setPageSize(pageInfo.getPageSize());
        int start = (pageInfo.getPageNo() - 1) * pageInfo.getPageSize();
        String loginId = LoginContextHolder.getLoginId();
        Integer count = practiceDao.getUnCompleteCount(loginId);
        if (count == 0) {
            return pageResult;
        }
        List<PracticePO> poList = practiceDao.getUnCompleteList(loginId, start, req.getPageInfo().getPageSize());
        if (log.isInfoEnabled()) {
            log.info("获取未完成的考卷列表{}", JSON.toJSONString(poList));
        }
        List<UnCompletePracticeSetVO> list = new LinkedList<>();
        poList.forEach(e -> {
            UnCompletePracticeSetVO vo = new UnCompletePracticeSetVO();
            vo.setSetId(e.getSetId());
            vo.setPracticeId(e.getId());
            vo.setPracticeTime(DateUtils.format(e.getSubmitTime(), "yyyy-MM-dd"));
            PracticeSetPO practiceSetPO = practiceSetDao.selectById(e.getSetId());
            vo.setTitle(practiceSetPO.getSetName());
            list.add(vo);
        });
        pageResult.setRecords(list);
        pageResult.setTotal(count);
        return pageResult;
    }

}
