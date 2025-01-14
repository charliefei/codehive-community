package com.feirui.subject.domain.service.impl;

import com.feirui.subject.common.context.LoginContextHolder;
import com.feirui.subject.common.entity.PageResult;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.common.utils.IdWorkerUtil;
import com.feirui.subject.domain.bo.SubjectInfoBO;
import com.feirui.subject.domain.bo.SubjectOptionBO;
import com.feirui.subject.domain.convert.SubjectInfoConverter;
import com.feirui.subject.domain.redis.RedisUtil;
import com.feirui.subject.domain.service.strategy.SubjectTypeHandlerFactory;
import com.feirui.subject.domain.service.strategy.handler.SubjectTypeHandler;
import com.feirui.subject.infra.basic.entity.SubjectInfo;
import com.feirui.subject.infra.basic.entity.SubjectInfoEs;
import com.feirui.subject.infra.basic.entity.SubjectLabel;
import com.feirui.subject.infra.basic.entity.SubjectMapping;
import com.feirui.subject.infra.basic.service.SubjectEsService;
import com.feirui.subject.infra.basic.service.SubjectInfoService;
import com.feirui.subject.infra.basic.service.SubjectLabelService;
import com.feirui.subject.infra.basic.service.SubjectMappingService;
import com.feirui.subject.infra.entity.UserInfo;
import com.feirui.subject.infra.rpc.UserRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectInfoDomainService {
    private static final String RANK_KEY = "subject_rank";
    @Resource
    private SubjectInfoService subjectInfoService;
    @Resource
    private SubjectTypeHandlerFactory subjectTypeHandlerFactory;
    @Resource
    private SubjectMappingService subjectMappingService;
    @Resource
    private SubjectLabelService subjectLabelService;
    @Resource
    private SubjectEsService subjectEsService;
    @Resource
    private SubjectLikedDomainService subjectLikedDomainService;
    @Resource
    private UserRpc userRpc;
    @Resource
    private RedisUtil redisUtil;

    @Transactional(rollbackFor = Exception.class)
    public void add(SubjectInfoBO bo) {
        SubjectInfo subjectInfo = SubjectInfoConverter.INSTANCE.convert(bo);
        subjectInfo.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
        // 存储公共信息
        subjectInfoService.save(subjectInfo);

        // 不同类型题目交给不同策略类存储特有信息
        SubjectTypeHandler handler = subjectTypeHandlerFactory.getHandler(subjectInfo.getSubjectType());
        bo.setId(subjectInfo.getId());
        handler.add(bo);

        // 存储题目分类、题目标签、题目三者的映射关系
        List<Integer> categoryIds = bo.getCategoryIds();
        List<Integer> labelIds = bo.getLabelIds();
        List<SubjectMapping> subjectMappingList = new LinkedList<>();
        // (1*n*n)条数据: 1一个题目 -> n个分类 -> n个标签
        categoryIds.forEach(categoryId -> {
            labelIds.forEach(labelId -> {
                SubjectMapping subjectMapping = new SubjectMapping();
                subjectMapping.setSubjectId(subjectInfo.getId());
                subjectMapping.setCategoryId(Long.valueOf(categoryId));
                subjectMapping.setLabelId(Long.valueOf(labelId));
                subjectMapping.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
                subjectMappingList.add(subjectMapping);
            });
        });
        subjectMappingService.saveBatch(subjectMappingList);
        // 同步题目数据到es
        SubjectInfoEs subjectInfoEs = new SubjectInfoEs();
        subjectInfoEs.setDocId(new IdWorkerUtil(1, 1, 1).nextId());
        subjectInfoEs.setSubjectId(subjectInfo.getId());
        subjectInfoEs.setSubjectAnswer(bo.getSubjectAnswer());
        subjectInfoEs.setCreateTime(new Date().getTime());
        subjectInfoEs.setCreateUser(LoginContextHolder.getLoginId());
        subjectInfoEs.setSubjectName(subjectInfo.getSubjectName());
        subjectInfoEs.setSubjectType(subjectInfo.getSubjectType());
        subjectEsService.insert(subjectInfoEs);
        // 每次有人出题，用zset维护一个出题排行榜，score是出题数量，value是出题人loginId
        redisUtil.addScore(RANK_KEY, LoginContextHolder.getLoginId(), 1);
    }

    public PageResult<SubjectInfoBO> getSubjectPage(SubjectInfoBO bo) {
        PageResult<SubjectInfoBO> pageResult = new PageResult<>();
        pageResult.setPageSize(bo.getPageSize());
        pageResult.setPageNo(bo.getPageNo());
        // 查询该难度下题目在数据库中总条数
        SubjectInfo subjectInfo = SubjectInfoConverter.INSTANCE.convert(bo);
        int total = subjectInfoService.countByCondition(subjectInfo,
                bo.getCategoryId(), bo.getLabelId());
        if (total == 0) {
            return pageResult;
        }

        // 计算该页在数据库中的第几条开始分页
        int start = (bo.getPageNo() - 1) * bo.getPageSize();
        // 按条件查询题目
        List<SubjectInfo> subjectInfoList = subjectInfoService.queryPage(
                subjectInfo,
                bo.getCategoryId(),
                bo.getLabelId(),
                start,
                bo.getPageSize()
        );
        List<SubjectInfoBO> boList = SubjectInfoConverter.INSTANCE.convert(subjectInfoList);
        boList.forEach(info -> {
            List<String> labelNames = getLabelNameList(info.getId());
            info.setLabelName(labelNames);
        });
        pageResult.setRecords(boList);
        return pageResult;
    }

    public SubjectInfoBO querySubjectInfo(SubjectInfoBO subjectInfoBO) {
        SubjectInfo subjectInfo = subjectInfoService.getById(subjectInfoBO.getId());

        SubjectTypeHandler handler = subjectTypeHandlerFactory.getHandler(subjectInfo.getSubjectType());
        SubjectOptionBO optionBO = handler.query(subjectInfo.getId());
        SubjectInfoBO infoBO = SubjectInfoConverter.INSTANCE.convert(optionBO);

        List<String> labelNameList = getLabelNameList(subjectInfo.getId());
        infoBO.setLabelName(labelNameList);
        infoBO.setLiked(subjectLikedDomainService.isLiked(subjectInfoBO.getId().toString(), LoginContextHolder.getLoginId()));
        infoBO.setLikedCount(subjectLikedDomainService.getLikedCount(subjectInfoBO.getId().toString()));
        assembleSubjectCursor(subjectInfoBO, infoBO);
        return infoBO;
    }

    /**
     * 获取当前题目的上一题和下一题的主键id
     */
    private void assembleSubjectCursor(SubjectInfoBO boReq, SubjectInfoBO boResp) {
        Long categoryId = boReq.getCategoryId();
        Long labelId = boReq.getLabelId();
        Long subjectId = boReq.getId();
        if (Objects.isNull(categoryId) || Objects.isNull(labelId) || Objects.isNull(subjectId)) {
            return;
        }
        Long nextSubjectId = subjectInfoService.querySubjectIdCursor(subjectId, categoryId, labelId, 1);
        boResp.setNextSubjectId(nextSubjectId);
        Long lastSubjectId = subjectInfoService.querySubjectIdCursor(subjectId, categoryId, labelId, 0);
        boResp.setLastSubjectId(lastSubjectId);
    }

    private List<String> getLabelNameList(Long subjectId) {
        List<SubjectMapping> mappingList = subjectMappingService
                .queryMappingsBySubjectId(subjectId);
        if (CollectionUtils.isEmpty(mappingList)) {
            return Collections.emptyList();
        }
        List<Long> labelIdList = mappingList.stream()
                .map(SubjectMapping::getLabelId)
                .distinct()
                .collect(Collectors.toList());
        List<SubjectLabel> labelList = subjectLabelService.queryLabelsByIds(labelIdList);
        return labelList.stream()
                .map(SubjectLabel::getLabelName)
                .collect(Collectors.toList());
    }

    public PageResult<SubjectInfoEs> getSubjectPageBySearch(SubjectInfoBO subjectInfoBO) {
        SubjectInfoEs subjectInfoEs = new SubjectInfoEs();
        subjectInfoEs.setPageNo(subjectInfoBO.getPageNo());
        subjectInfoEs.setPageSize(subjectInfoBO.getPageSize());
        subjectInfoEs.setKeyWord(subjectInfoBO.getKeyWord());
        return subjectEsService.querySubjectList(subjectInfoEs);
    }

    public List<SubjectInfoBO> getContributeList() {
        Set<ZSetOperations.TypedTuple<String>> rankZSet = redisUtil.rankWithScore(RANK_KEY, 0, 5);
        if (log.isInfoEnabled()) {
            log.info("getContributeList.rankZSet: {}", rankZSet);
        }
        if (CollectionUtils.isEmpty(rankZSet)) {
            return Collections.emptyList();
        }
        return rankZSet.stream().map(item -> {
            SubjectInfoBO subjectInfoBO = new SubjectInfoBO();
            subjectInfoBO.setSubjectCount(item.getScore().intValue());
            UserInfo userInfo = userRpc.getUserInfo(item.getValue());
            subjectInfoBO.setCreateUser(userInfo.getNickName());
            subjectInfoBO.setCreateUserAvatar(userInfo.getAvatar());
            return subjectInfoBO;
        }).collect(Collectors.toList());

        // List<SubjectInfo>  contributeCountList = subjectInfoService.getContributeCountList();
        // if (CollectionUtils.isEmpty(contributeCountList)) {
        //     return Collections.emptyList();
        // }
        // return contributeCountList.stream().map(item -> {
        //     SubjectInfoBO subjectInfoBO = new SubjectInfoBO();
        //     subjectInfoBO.setSubjectCount(item.getSubjectCount());
        //     // createBy也就是出题人的loginId，即数据库里面的userName字段，微信扫码获取的openId
        //     UserInfo userInfo = userRpc.getUserInfo(item.getCreatedBy());
        //     subjectInfoBO.setCreateUser(userInfo.getNickName());
        //     subjectInfoBO.setCreateUserAvatar(userInfo.getAvatar());
        //     return subjectInfoBO;
        // }).collect(Collectors.toList());
    }
}
