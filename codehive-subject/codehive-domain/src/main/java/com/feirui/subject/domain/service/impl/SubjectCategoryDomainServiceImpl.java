package com.feirui.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.domain.bo.SubjectCategoryBO;
import com.feirui.subject.domain.bo.SubjectLabelBO;
import com.feirui.subject.domain.convert.SubjectCategoryConverter;
import com.feirui.subject.domain.convert.SubjectLabelConverter;
import com.feirui.subject.domain.service.SubjectCategoryDomainService;
import com.feirui.subject.domain.utils.CacheUtil;
import com.feirui.subject.infra.basic.entity.SubjectCategory;
import com.feirui.subject.infra.basic.entity.SubjectLabel;
import com.feirui.subject.infra.basic.entity.SubjectMapping;
import com.feirui.subject.infra.basic.service.SubjectCategoryService;
import com.feirui.subject.infra.basic.service.impl.SubjectLabelServiceImpl;
import com.feirui.subject.infra.basic.service.impl.SubjectMappingServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectCategoryDomainServiceImpl implements SubjectCategoryDomainService {
    @Resource
    private SubjectCategoryService subjectCategoryService;
    @Resource
    private SubjectMappingServiceImpl subjectMappingService;
    @Resource
    private SubjectLabelServiceImpl subjectLabelService;
    @Resource
    private ThreadPoolExecutor labelThreadPool;

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
        List<SubjectCategoryBO> boList = SubjectCategoryConverter.INSTANCE.convert(categories);
        // 查询每一分类下的题目数量
        boList.forEach(categoryBO -> {
            Integer subjectCount = subjectCategoryService.querySubjectCount(categoryBO.getId());
            categoryBO.setCount(subjectCount);
        });
        return boList;
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

    @Override
    public List<SubjectCategoryBO> queryCategoryAndLabel(SubjectCategoryBO subjectCategoryBO) {
        Long id = subjectCategoryBO.getId();
        String cacheKey = "categoryAndLabel." + id;
        return CacheUtil.getCacheValue(cacheKey, SubjectCategoryBO.class,
                () -> getSubjectCategoryBOS(id));
    }

    @SneakyThrows
    private List<SubjectCategoryBO> getSubjectCategoryBOS(Long categoryId) {
        // 查询该一级分类下的所有二级分类
        SubjectCategory subjectCategory = new SubjectCategory();
        subjectCategory.setParentId(categoryId);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        List<SubjectCategory> categoryList = subjectCategoryService.queryCategory(subjectCategory);
        List<SubjectCategoryBO> categoryBOList = SubjectCategoryConverter.INSTANCE.convert(categoryList);

        // 查询每一个二级分类下的所有标签
        // todo 后续使用completableFuture优化一下，参考mallchat
        List<FutureTask<Map<Long, List<SubjectLabelBO>>>> futureTasks = new LinkedList<>();
        Map<Long, List<SubjectLabelBO>> map = new HashMap<>();
        categoryBOList.forEach(categoryBO -> {
            FutureTask<Map<Long, List<SubjectLabelBO>>> futureTask = new FutureTask<>(() -> getLabelBOMap(categoryBO));
            futureTasks.add(futureTask);
            labelThreadPool.submit(futureTask);
        });
        for (FutureTask<Map<Long, List<SubjectLabelBO>>> futureTask : futureTasks) {
            Map<Long, List<SubjectLabelBO>> resultMap = futureTask.get();
            if (CollectionUtils.isEmpty(resultMap)) continue;
            map.putAll(resultMap);
        }
        categoryBOList.forEach(categoryBO -> {
            if (!CollectionUtils.isEmpty(map.get(categoryBO.getId()))) {
                categoryBO.setLabelBOList(map.get(categoryBO.getId()));
            }
        });

        return categoryBOList;
    }

    private Map<Long, List<SubjectLabelBO>> getLabelBOMap(SubjectCategoryBO categoryBO) {
        Map<Long, List<SubjectLabelBO>> labelBOMap = new HashMap<>();
        List<SubjectMapping> mappingList = subjectMappingService.queryMappingsByCategoryId(categoryBO.getId());
        if (CollectionUtils.isEmpty(mappingList)) return labelBOMap;
        List<Long> labelIdList = mappingList.stream().map(SubjectMapping::getLabelId).collect(Collectors.toList());
        List<SubjectLabel> labelList = subjectLabelService.queryLabelsByIds(labelIdList);
        List<SubjectLabelBO> labelBOList = SubjectLabelConverter.INSTANCE.convert(labelList);
        labelBOMap.put(categoryBO.getId(), labelBOList);
        return labelBOMap;
    }
}
