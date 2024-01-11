package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.infra.basic.dao.SubjectJudgeDao;
import com.feirui.subject.infra.basic.entity.SubjectJudge;
import com.feirui.subject.infra.basic.service.SubjectJudgeService;
import org.springframework.stereotype.Service;

/**
 * 判断题(SubjectJudge)表服务实现类
 *
 * @author makejava
 * @since 2023-12-29 17:23:10
 */
@Service("subjectJudgeService")
public class SubjectJudgeServiceImpl extends ServiceImpl<SubjectJudgeDao, SubjectJudge> implements SubjectJudgeService {

}

