package com.feirui.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.feirui.subject.application.convert.SubjectAnswerDTOConverter;
import com.feirui.subject.application.convert.SubjectInfoDTOConverter;
import com.feirui.subject.application.dto.SubjectInfoDTO;
import com.feirui.subject.common.entity.PageResult;
import com.feirui.subject.common.entity.Result;
import com.feirui.subject.domain.bo.SubjectAnswerBO;
import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.domain.service.impl.SubjectInfoDomainService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/subject")
@Slf4j
public class SubjectController {
    @Resource
    private SubjectInfoDomainService subjectInfoDomainService;

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody SubjectInfoDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("add.dto {}", JSON.toJSONString(dto));
            }
            Preconditions.checkArgument(!StringUtils.isBlank(dto.getSubjectName()),
                    "题目名称不能为空");
            Preconditions.checkNotNull(dto.getSubjectDifficult(), "题目难度不能为空");
            Preconditions.checkNotNull(dto.getSubjectType(), "题目类型不能为空");
            Preconditions.checkNotNull(dto.getSubjectScore(), "题目分数不能为空");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(dto.getCategoryIds())
                    , "分类id不能为空");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(dto.getLabelIds())
                    , "标签id不能为空");

            SubjectInfoBO subjectInfoBO = SubjectInfoDTOConverter.INSTANCE.convert(dto);
            List<SubjectAnswerBO> subjectAnswerBOS =
                    SubjectAnswerDTOConverter.INSTANCE.convert(dto.getOptionList());
            subjectInfoBO.setOptionList(subjectAnswerBOS);
            subjectInfoDomainService.add(subjectInfoBO);
            return Result.ok(true);
        } catch (Exception e) {
            log.error("add.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/getSubjectPage")
    public Result<PageResult<SubjectInfoDTO>> getSubjectPage(@RequestBody SubjectInfoDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("getSubjectPage.dto {}", JSON.toJSONString(dto));
            }
            Preconditions.checkNotNull(dto.getCategoryId(), "分类id不可为空");
            Preconditions.checkNotNull(dto.getLabelId(), "标签id不可为空");
            SubjectInfoBO bo = SubjectInfoDTOConverter.INSTANCE.convert(dto);
            PageResult<SubjectInfoBO> boPageResult = subjectInfoDomainService.getSubjectPage(bo);
            PageResult<SubjectInfoDTO> dtoPageResult = SubjectInfoDTOConverter.INSTANCE.convert(boPageResult);
            return Result.ok(dtoPageResult);
        } catch (Exception e) {
            log.error("getSubjectPage.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询题目信息
     */
    @PostMapping("/querySubjectInfo")
    public Result<SubjectInfoDTO> querySubjectInfo(@RequestBody SubjectInfoDTO subjectInfoDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectController.querySubjectInfo.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }
            Preconditions.checkNotNull(subjectInfoDTO.getId(), "题目id不能为空");
            SubjectInfoBO subjectInfoBO = SubjectInfoDTOConverter.INSTANCE.convert(subjectInfoDTO);
            SubjectInfoBO boResult = subjectInfoDomainService.querySubjectInfo(subjectInfoBO);
            SubjectInfoDTO dto = SubjectInfoDTOConverter.INSTANCE.convert(boResult);
            return Result.ok(dto);
        } catch (Exception e) {
            log.error("querySubjectInfo.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }
}
