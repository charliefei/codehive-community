package com.feirui.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.feirui.subject.common.context.LoginContextHolder;
import com.feirui.subject.common.entity.PageResult;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.common.enums.SubjectLikedStatusEnum;
import com.feirui.subject.domain.bo.SubjectLikedBO;
import com.feirui.subject.domain.convert.SubjectLikedBOConverter;
import com.feirui.subject.domain.redis.RedisUtil;
import com.feirui.subject.infra.basic.entity.SubjectInfo;
import com.feirui.subject.infra.basic.entity.SubjectLiked;
import com.feirui.subject.infra.basic.service.SubjectInfoService;
import com.feirui.subject.infra.basic.service.SubjectLikedService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 题目点赞表 领域service实现了
 *
 * @author charliefei
 * @since 2025-01-10 00:37:45
 */
@Slf4j
@Service
public class SubjectLikedDomainService {

    /**
     * 记录所有的点赞记录，后续同步该key下的所有数据到数据库
     */
    private static final String SUBJECT_LIKED_KEY = "subject.liked";
    /**
     * 记录某道题是否被某个人点赞
     */
    private static final String SUBJECT_LIKED_COUNT_KEY = "subject.liked.count";
    /**
     * 记录某道题被点赞的次数
     */
    private static final String SUBJECT_LIKED_DETAIL_KEY = "subject.liked.detail";

    @Resource
    private SubjectLikedService subjectLikedService;
    @Resource
    private SubjectInfoService subjectInfoService;
    @Resource
    private RedisUtil redisUtil;


    /**
     * 添加 题目点赞表 信息
     */
    public void add(SubjectLikedBO subjectLikedBO) {
        Long subjectId = subjectLikedBO.getSubjectId();
        String likeUserId = subjectLikedBO.getLikeUserId();
        Integer status = subjectLikedBO.getStatus();

        String hashKey = subjectId + ":" + likeUserId;
        redisUtil.putHash(SUBJECT_LIKED_KEY, hashKey, status);

        String detailKey = redisUtil.buildKey(SUBJECT_LIKED_DETAIL_KEY, subjectId.toString(), likeUserId);
        String countKey = redisUtil.buildKey(SUBJECT_LIKED_COUNT_KEY, subjectId.toString());
        if (SubjectLikedStatusEnum.LIKED.getCode() == status) {
            redisUtil.increment(countKey, 1);
            redisUtil.set(detailKey, "1");
        } else {
            Integer count = redisUtil.getInt(countKey);
            if (Objects.isNull(count) || count <= 0) {
                return;
            }
            redisUtil.increment(countKey, -1);
            redisUtil.del(detailKey);
        }
    }

    /**
     * 获取当前是否被点赞过
     */
    public Boolean isLiked(String subjectId, String userId) {
        String detailKey = SUBJECT_LIKED_DETAIL_KEY + "." + subjectId + "." + userId;
        return redisUtil.exist(detailKey);
    }

    /**
     * 获取点赞数量
     */
    public Integer getLikedCount(String subjectId) {
        String countKey = SUBJECT_LIKED_COUNT_KEY + "." + subjectId;
        Integer count = redisUtil.getInt(countKey);
        if (Objects.isNull(count) || count <= 0) {
            return 0;
        }
        return redisUtil.getInt(countKey);
    }

    /**
     * 更新 题目点赞表 信息
     */
    public Boolean update(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convert(subjectLikedBO);
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

    /**
     * 同步点赞数据
     */
    public void syncLiked() {
        Map<Object, Object> subjectLikedMap = redisUtil.getHashAndDelete(SUBJECT_LIKED_KEY);
        if (log.isInfoEnabled()) {
            log.info("syncLiked.subjectLikedMap:{}", JSON.toJSONString(subjectLikedMap));
        }
        if (MapUtils.isEmpty(subjectLikedMap)) {
            return;
        }
        // 批量同步到数据库
        List<SubjectLiked> subjectLikedList = new LinkedList<>();
        subjectLikedMap.forEach((key, val) -> {
            SubjectLiked subjectLiked = new SubjectLiked();
            String[] keyArr = key.toString().split(":");
            String subjectId = keyArr[0];
            String likedUser = keyArr[1];
            subjectLiked.setSubjectId(Long.valueOf(subjectId));
            subjectLiked.setLikeUserId(likedUser);
            subjectLiked.setStatus(Integer.valueOf(val.toString()));
            subjectLiked.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getStatus());
            subjectLikedList.add(subjectLiked);
        });
        subjectLikedService.saveBatch(subjectLikedList);
    }

    public PageResult<SubjectLikedBO> getSubjectLikedPage(SubjectLikedBO subjectLikedBO) {
        PageResult<SubjectLikedBO> pageResult = new PageResult<>();

        Integer count = subjectLikedService.countByLikedUserId(LoginContextHolder.getLoginId());
        if (count == 0) {
            return pageResult;
        }

        int start = (subjectLikedBO.getPageNo() - 1) * subjectLikedBO.getPageSize();
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convert(subjectLikedBO);
        subjectLiked.setLikeUserId(LoginContextHolder.getLoginId());
        List<SubjectLiked> subjectLikedList = subjectLikedService.queryPage(subjectLiked, start, subjectLikedBO.getPageSize());
        List<SubjectLikedBO> subjectInfoBOS = SubjectLikedBOConverter.INSTANCE.convert(subjectLikedList);
        subjectInfoBOS.forEach(info -> {
            SubjectInfo subjectInfo = subjectInfoService.getById(info.getSubjectId());
            info.setSubjectName(subjectInfo.getSubjectName());
        });
        pageResult.setPageNo(subjectLikedBO.getPageNo());
        pageResult.setPageSize(subjectLikedBO.getPageSize());
        pageResult.setRecords(subjectInfoBOS);
        pageResult.setTotal(count);
        return pageResult;
    }

}
