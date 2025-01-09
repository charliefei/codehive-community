package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.infra.basic.dao.SubjectLikedDao;
import com.feirui.subject.infra.basic.entity.SubjectLiked;
import com.feirui.subject.infra.basic.service.SubjectLikedService;
import org.springframework.stereotype.Service;

/**
 * 题目点赞表(SubjectLiked)表服务实现类
 *
 * @author charliefei
 * @since 2025-01-10 00:24:48
 */
@Service("subjectLikedService")
public class SubjectLikedServiceImpl extends ServiceImpl<SubjectLikedDao, SubjectLiked> implements SubjectLikedService {

}

