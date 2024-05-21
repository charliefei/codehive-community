package com.feirui.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.feirui.subject.application.convert.SubjectCategoryDTOConverter;
import com.feirui.subject.application.convert.SubjectLabelDTOConverter;
import com.feirui.subject.application.dto.SubjectCategoryDTO;
import com.feirui.subject.application.dto.SubjectLabelDTO;
import com.feirui.subject.common.entity.Result;
import com.feirui.subject.common.enums.CategoryTypeEnum;
import com.feirui.subject.domain.bo.SubjectCategoryBO;
import com.feirui.subject.domain.service.SubjectCategoryDomainService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject/category")
@Slf4j
public class SubjectCategoryController {
    @Resource
    private SubjectCategoryDomainService subjectCategoryDomainService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody SubjectCategoryDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("add.dto {}", JSON.toJSONString(dto));
            }
            // guava断言工具类
            Preconditions.checkNotNull(dto.getCategoryType(), "分类类型不能为空");
            Preconditions.checkArgument(CategoryTypeEnum.types().contains(dto.getCategoryType()), "无效的分类类型");
            Preconditions.checkArgument(!StringUtils.isBlank(dto.getCategoryName()), "分类名称不能为空");
            Preconditions.checkNotNull(dto.getParentId(), "分类父级id不能为空");
            // todo 防御性编程：parentId只能是0(代表是一级分类)或者分类表中的已经存在的分类id(二级分类)
            // mapStructs对象属性拷贝
            SubjectCategoryBO categoryBO = SubjectCategoryDTOConverter.INSTANCE
                    .convert(dto);
            subjectCategoryDomainService.add(categoryBO);
            return Result.ok(true);
        } catch (Exception e) {
            log.error("add.error {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/queryPrimaryCategory")
    public Result<List<SubjectCategoryDTO>> queryPrimaryCategory(@RequestBody SubjectCategoryDTO dto) {
        try {
            Preconditions.checkNotNull(dto.getCategoryType(), "分类类型不能为空");
            Preconditions.checkArgument(CategoryTypeEnum.types().contains(dto.getCategoryType()), "无效的分类类型");
            SubjectCategoryBO bo = SubjectCategoryDTOConverter.INSTANCE
                    .convert(dto);
            List<SubjectCategoryBO> bos = subjectCategoryDomainService.queryCategory(bo);
            List<SubjectCategoryDTO> dtoList = SubjectCategoryDTOConverter.INSTANCE
                    .convert(bos);
            return Result.ok(dtoList);
        } catch (Exception e) {
            log.error("queryPrimaryCategory.error {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/queryCategoryByPrimary")
    public Result<List<SubjectCategoryDTO>> queryCategoryByPrimary(@RequestBody SubjectCategoryDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("queryCategoryByPrimary.dto {}", JSON.toJSONString(dto));
            }
            Preconditions.checkNotNull(dto.getParentId(), "父类id不可为空！");
            SubjectCategoryBO bo = SubjectCategoryDTOConverter.INSTANCE
                    .convert(dto);
            List<SubjectCategoryBO> boList = subjectCategoryDomainService.queryCategory(bo);
            List<SubjectCategoryDTO> dtoList = SubjectCategoryDTOConverter.INSTANCE
                    .convert(boList);
            return Result.ok(dtoList);
        } catch (Exception e) {
            log.error("queryCategoryByPrimary.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询分类及标签一次性
     */
    @PostMapping("/queryCategoryAndLabel")
    public Result<List<SubjectCategoryDTO>> queryCategoryAndLabel(@RequestBody SubjectCategoryDTO subjectCategoryDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectCategoryController.queryCategoryAndLabel.dto:{}"
                        , JSON.toJSONString(subjectCategoryDTO));
            }
            // 传过来的id只能是一级分类的id
            Preconditions.checkNotNull(subjectCategoryDTO.getId(), "分类id不能为空");
            SubjectCategoryBO subjectCategoryBO = SubjectCategoryDTOConverter.INSTANCE.
                    convert(subjectCategoryDTO);
            List<SubjectCategoryBO> categoryBOList = subjectCategoryDomainService
                    .queryCategoryAndLabel(subjectCategoryBO);
            List<SubjectCategoryDTO> dtoList = categoryBOList.stream().map(bo -> {
                SubjectCategoryDTO dto = SubjectCategoryDTOConverter.INSTANCE.convert(bo);
                List<SubjectLabelDTO> labelDTOList = SubjectLabelDTOConverter.INSTANCE.convert(bo.getLabelBOList());
                dto.setLabelDTOList(labelDTOList);
                return dto;
            }).collect(Collectors.toList());
            return Result.ok(dtoList);
        } catch (Exception e) {
            log.error("SubjectCategoryController.queryPrimaryCategory.error:{}", e.getMessage(), e);
            return Result.fail("查询失败");
        }
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody SubjectCategoryDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("update.dto {}", JSON.toJSONString(dto));
            }
            SubjectCategoryBO bo = SubjectCategoryDTOConverter.INSTANCE
                    .convert(dto);
            Boolean res = subjectCategoryDomainService.update(bo);
            return Result.ok(res);
        } catch (Exception e) {
            log.error("update.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody SubjectCategoryDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("delete.dto {}", JSON.toJSONString(dto));
            }
            SubjectCategoryBO bo = SubjectCategoryDTOConverter.INSTANCE
                    .convert(dto);
            Boolean res = subjectCategoryDomainService.delete(bo);
            return Result.ok(res);
        } catch (Exception e) {
            log.error("update.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }
}
