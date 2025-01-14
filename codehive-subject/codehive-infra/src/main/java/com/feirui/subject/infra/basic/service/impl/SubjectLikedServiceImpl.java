package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.infra.basic.dao.SubjectLikedDao;
import com.feirui.subject.infra.basic.entity.SubjectLiked;
import com.feirui.subject.infra.basic.service.SubjectLikedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目点赞表(SubjectLiked)表服务实现类
 *
 * @author charliefei
 * @since 2025-01-10 00:24:48
 */
@Service("subjectLikedService")
public class SubjectLikedServiceImpl extends ServiceImpl<SubjectLikedDao, SubjectLiked> implements SubjectLikedService {

    @Resource
    private SubjectLikedDao subjectLikedDao;

    @Override
    public Integer countByLikedUserId(String likedUserId) {
        return lambdaQuery()
                .eq(SubjectLiked::getLikeUserId, likedUserId)
                .eq(SubjectLiked::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getStatus())
                .eq(SubjectLiked::getStatus, 1)
                .count();
    }

    @Override
    public List<SubjectLiked> queryPage(SubjectLiked subjectLiked, int start, Integer pageSize) {
        return subjectLikedDao.queryPage(subjectLiked, start, pageSize);
    }

}

