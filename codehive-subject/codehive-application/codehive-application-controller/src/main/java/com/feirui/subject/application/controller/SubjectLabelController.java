package com.feirui.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.feirui.subject.application.convert.SubjectLabelDTOConverter;
import com.feirui.subject.application.dto.SubjectLabelDTO;
import com.feirui.subject.common.entity.Result;
import com.feirui.subject.domain.bo.SubjectLabelBO;
import com.feirui.subject.domain.service.impl.SubjectLabelDomainService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/subject/label")
@Slf4j
public class SubjectLabelController {
    @Resource
    private SubjectLabelDomainService subjectLabelDomainService;

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody SubjectLabelDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("add.dto {}", JSON.toJSONString(dto));
            }
            Preconditions.checkNotNull(dto.getLabelName(), "标签名称不可为空");
            SubjectLabelBO bo = SubjectLabelDTOConverter.INSTANCE.convert(dto);
            subjectLabelDomainService.add(bo);
            return Result.ok(true);
        } catch (Exception e) {
            log.error("add.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody SubjectLabelDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("update.dto {}", JSON.toJSONString(dto));
            }
            Preconditions.checkNotNull(dto.getId(), "标签id不可为空");
            SubjectLabelBO bo = SubjectLabelDTOConverter.INSTANCE.convert(dto);
            subjectLabelDomainService.update(bo);
            return Result.ok(true);
        } catch (Exception e) {
            log.error("update.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody SubjectLabelDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("delete.dto {}", JSON.toJSONString(dto));
            }
            Preconditions.checkNotNull(dto.getId(), "标签id不可为空");
            SubjectLabelBO bo = SubjectLabelDTOConverter.INSTANCE.convert(dto);
            subjectLabelDomainService.delete(bo);
            return Result.ok(true);
        } catch (Exception e) {
            log.error("delete.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/queryLabelByCategoryId")
    public Result<List<SubjectLabelDTO>> queryLabelByCategoryId(@RequestBody SubjectLabelDTO dto) {
        try {
            if (log.isInfoEnabled()) {
                log.info("queryLabelByCategoryId.dto {}", JSON.toJSONString(dto));
            }
            Preconditions.checkNotNull(dto.getCategoryId(), "分类id不可为空");
            SubjectLabelBO bo = SubjectLabelDTOConverter.INSTANCE
                    .convert(dto);
            List<SubjectLabelBO> boList = subjectLabelDomainService.queryLabelByCategoryId(bo);
            List<SubjectLabelDTO> dtoList = SubjectLabelDTOConverter.INSTANCE
                    .convert(boList);
            return Result.ok(dtoList);
        } catch (Exception e) {
            log.error("queryLabelByCategoryId.err {}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }
}
