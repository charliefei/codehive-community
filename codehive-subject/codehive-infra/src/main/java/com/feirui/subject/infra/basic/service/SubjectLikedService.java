package com.feirui.subject.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.subject.infra.basic.entity.SubjectLiked;

import java.util.List;

/**
 * 题目点赞表(SubjectLiked)表服务接口
 *
 * @author charliefei
 * @since 2025-01-10 00:24:47
 */
public interface SubjectLikedService extends IService<SubjectLiked> {

    Integer countByLikedUserId(String likedUserId);

    List<SubjectLiked> queryPage(SubjectLiked subjectLiked, int start, Integer pageSize);
}

