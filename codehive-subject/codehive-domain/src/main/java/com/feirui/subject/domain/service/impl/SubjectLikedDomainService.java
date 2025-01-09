package com.feirui.subject.domain.service.impl;

import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.domain.bo.SubjectLikedBO;
import com.feirui.subject.domain.convert.SubjectLikedBOConverter;
import com.feirui.subject.infra.basic.entity.SubjectLiked;
import com.feirui.subject.infra.basic.service.SubjectLikedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 题目点赞表 领域service实现了
 *
 * @author charliefei
 * @since 2025-01-10 00:37:45
 */
@Slf4j
@Service
public class SubjectLikedDomainService {

    @Resource
    private SubjectLikedService subjectLikedService;

    /**
     * 添加 题目点赞表 信息
     */
    public Boolean add(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        subjectLiked.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        return subjectLikedService.save(subjectLiked);
    }

    /**
     * 更新 题目点赞表 信息
     */
    public Boolean update(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        return subjectLikedService.updateById(subjectLiked);
    }

    /**
     * 删除 题目点赞表 信息
     */
    public Boolean delete(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = new SubjectLiked();
        subjectLiked.setId(subjectLikedBO.getId());
        subjectLiked.setIsDeleted(IsDeletedFlagEnum.DELETED.getStatus());
        return subjectLikedService.updateById(subjectLiked);
    }

}
